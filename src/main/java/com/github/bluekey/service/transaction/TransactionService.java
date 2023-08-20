package com.github.bluekey.service.transaction;

import com.amazonaws.services.s3.model.S3Object;
import com.github.bluekey.dto.request.transaction.OriginalTransactionRequestDto;
import com.github.bluekey.dto.response.ListResponse;
import com.github.bluekey.dto.response.transaction.OriginalTransactionResponseDto;
import com.github.bluekey.entity.transaction.OriginalTransaction;
import com.github.bluekey.exception.transaction.ExcelUploadException;
import com.github.bluekey.processor.ExcelFileProcessManager;
import com.github.bluekey.processor.ExcelRowException;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
import com.github.bluekey.repository.track.TrackRepository;
import com.github.bluekey.repository.transaction.OriginalTransactionRepository;
import com.github.bluekey.s3.manager.AwsS3Manager;
import com.github.bluekey.s3.manager.S3PrefixType;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

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

    public ListResponse<OriginalTransactionResponseDto> getOriginalTransactions(String uploadAt) {
        List<OriginalTransaction> originalTransactions = originalTransactionRepository.findAllByUploadAt(uploadAt);
        return new ListResponse<>(originalTransactions.size(), originalTransactions.stream().map(OriginalTransactionResponseDto::from).collect(Collectors.toList()));
    }

    public OriginalTransactionResponseDto removeOriginalTransaction(Long id) {
        OriginalTransaction originalTransaction = originalTransactionRepository.findByIdOrElseThrow(id);

        // 삭제 로직

        return OriginalTransactionResponseDto.from(originalTransaction);
    }

    public OriginalTransactionResponseDto saveOriginalTransaction(MultipartFile file, OriginalTransactionRequestDto requestDto) {
        String uploadAt = requestDto.getUploadAt();

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

        String s3Url = awsS3Manager.upload(file, uploadAt + "/" + file.getOriginalFilename(), S3PrefixType.EXCEL);
        S3Object excelFileS3Object = awsS3Manager.getS3Value(S3PrefixType.EXCEL.getValue() + file.getOriginalFilename());

        Workbook workbook = getWorkBook(excelFileS3Object);

        OriginalTransaction originalTransaction = OriginalTransaction.builder()
                .uploadAt(requestDto.getUploadAt())
                .fileName(file.getOriginalFilename())
                .fileUrl(s3Url)
                .build();
        originalTransactionRepository.save(originalTransaction);
        return OriginalTransactionResponseDto.fromWithWarning(originalTransaction, excelFileProcessManager.getWarnings());
    }

    private Workbook getWorkBook(S3Object s3Object) {
        try (InputStream inputStream = s3Object.getObjectContent()) {
            return WorkbookFactory.create(inputStream);
        } catch (Exception e) {
            // 처리 중 발생한 예외 처리
            throw new RuntimeException("Error while reading Excel file from S3", e);
        }
    }
}
