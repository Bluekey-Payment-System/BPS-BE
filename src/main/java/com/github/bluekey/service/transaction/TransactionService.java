package com.github.bluekey.service.transaction;

import com.amazonaws.services.s3.model.S3Object;
import com.github.bluekey.dto.request.transaction.OriginalTransactionRequestDto;
import com.github.bluekey.dto.response.common.ListResponse;
import com.github.bluekey.dto.response.transaction.OriginalTransactionResponseDto;
import com.github.bluekey.entity.transaction.OriginalTransaction;
import com.github.bluekey.exception.transaction.AlreadyOriginalTransactionExistException;
import com.github.bluekey.exception.transaction.ExcelUploadException;
import com.github.bluekey.processor.ExcelFileDBMigrationProcessManager;
import com.github.bluekey.processor.ExcelFileProcessManager;
import com.github.bluekey.processor.ExcelRowException;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
import com.github.bluekey.repository.track.TrackRepository;
import com.github.bluekey.repository.transaction.OriginalTransactionRepository;
import com.github.bluekey.s3.manager.AwsS3Manager;
import com.github.bluekey.s3.manager.S3PrefixType;
import com.github.bluekey.util.ExcelUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private static final int ERROR_THRESHOLD = 0;
    private final OriginalTransactionRepository originalTransactionRepository;
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final TrackMemberRepository trackMemberRepository;
    private final AwsS3Manager awsS3Manager;
    private final ExcelUploadUtil excelUploadUtil;
    private final ExcelFileDBMigrationProcessManager excelFileDBMigrationProcessManager;

    public ListResponse<OriginalTransactionResponseDto> getOriginalTransactions(String uploadAt) {
        List<OriginalTransaction> originalTransactions = originalTransactionRepository.findAllByUploadAtAndIsRemovedFalse(uploadAt);
        return new ListResponse<>(originalTransactions.size(), originalTransactions.stream().map(OriginalTransactionResponseDto::from).collect(Collectors.toList()));
    }

    @Transactional
    public OriginalTransactionResponseDto removeOriginalTransaction(Long id) {
        OriginalTransaction originalTransaction = originalTransactionRepository.findByIdOrElseThrow(id);

        originalTransaction.remove();
        originalTransactionRepository.save(originalTransaction);
        excelUploadUtil.deleteExcel(excelUploadUtil.getExcelKey(originalTransaction.getFileName(), originalTransaction.getUploadAt()));

        originalTransaction.getTransactions()
                .forEach(transaction -> transaction.remove());

        return OriginalTransactionResponseDto.from(originalTransaction);
    }

    @Transactional
    public OriginalTransactionResponseDto saveOriginalTransaction(MultipartFile file, OriginalTransactionRequestDto requestDto) {
        String uploadAt = requestDto.getUploadMonth();
        String fileName = file.getOriginalFilename();

        ExcelFileProcessManager excelFileProcessManager = new ExcelFileProcessManager(
                file,
                memberRepository,
                albumRepository,
                trackRepository,
                trackMemberRepository
        );
        excelFileProcessManager.process();
        List<ExcelRowException> errors = excelFileProcessManager.getErrors();
        if (errors.size() > ERROR_THRESHOLD) {
            throw new ExcelUploadException(errors);
        }

        originalTransactionRepository.findOriginalTransactionByFileNameAndUploadAtAndIsRemovedFalse(fileName, uploadAt).ifPresent((originalTransaction) -> {
                    throw new AlreadyOriginalTransactionExistException();
                });

        String s3Url = excelUploadUtil.uploadExcel(file, excelUploadUtil.getExcelKey(file.getOriginalFilename(), uploadAt));


        OriginalTransaction originalTransaction = OriginalTransaction.builder()
                .uploadAt(requestDto.getUploadMonth())
                .fileName(file.getOriginalFilename())
                .fileUrl(s3Url)
                .distributorType(excelFileProcessManager.getDistributorType())
                .build();
        originalTransactionRepository.save(originalTransaction);
        ExcelFilesToDBMigration();
        return OriginalTransactionResponseDto.fromWithWarning(originalTransaction, excelFileProcessManager.getWarnings());
    }

    public void ExcelFilesToDBMigration() {
        Map<Workbook, OriginalTransaction> workbooks = new HashMap<>();
        List<OriginalTransaction> originalTransactions = originalTransactionRepository.findAllByIsCompletedFalseAndIsRemovedFalse();
        for (OriginalTransaction originalTransaction: originalTransactions) {
            String s3Key = awsS3Manager.getS3Key(originalTransaction.getFileUrl(), S3PrefixType.EXCEL);
            S3Object s3Object = excelUploadUtil.getExcel(s3Key);
            Workbook workbook = getWorkBook(s3Object);
            workbooks.put(workbook, originalTransaction);
        }
        excelFileDBMigrationProcessManager.updateWorkbooks(workbooks);
        excelFileDBMigrationProcessManager.process();
    }

    private Workbook getWorkBook(S3Object s3Object) {
        try (InputStream inputStream = s3Object.getObjectContent()) {
            return WorkbookFactory.create(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error while reading Excel file from S3", e);
        }
    }
}
