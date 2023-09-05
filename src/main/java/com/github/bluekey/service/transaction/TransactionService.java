package com.github.bluekey.service.transaction;

import com.amazonaws.services.s3.model.S3Object;
import com.github.bluekey.dto.request.transaction.OriginalTransactionRequestDto;
import com.github.bluekey.dto.response.common.ListResponse;
import com.github.bluekey.dto.response.transaction.OriginalTransactionResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.transaction.OriginalTransaction;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.exception.transaction.ExcelUploadException;
import com.github.bluekey.mail.EmailSender;
import com.github.bluekey.processor.ExcelFileDBMigrationProcessManager;
import com.github.bluekey.processor.ExcelFileProcessManager;
import com.github.bluekey.processor.ExcelRowException;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
import com.github.bluekey.repository.track.TrackRepository;
import com.github.bluekey.repository.transaction.OriginalTransactionRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;
import com.github.bluekey.s3.manager.AwsS3Manager;
import com.github.bluekey.s3.manager.S3PrefixType;
import com.github.bluekey.util.ExcelUploadUtil;
import java.io.IOException;
import javax.mail.MessagingException;
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
    private static final String DEV_TARGET_EMAIL = "wnl383@naver.com";
    private static final String DEV_TARGET_EMAIL_TITLE = "Bluekey music 정산 알림";
    private static final String DEV_TARGET_EMAIL_NAME = "조세영";
    private final OriginalTransactionRepository originalTransactionRepository;
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final TrackMemberRepository trackMemberRepository;
    private final AwsS3Manager awsS3Manager;
    private final ExcelUploadUtil excelUploadUtil;
    private final ExcelFileDBMigrationProcessManager excelFileDBMigrationProcessManager;
    private final EmailSender emailSender;

    @Transactional(readOnly = true)
    public ListResponse<OriginalTransactionResponseDto> getOriginalTransactions(String uploadAt) {
        List<OriginalTransaction> originalTransactions = originalTransactionRepository.findAllByUploadAtAndIsRemovedFalse(uploadAt);
        return new ListResponse<>(originalTransactions.size(), originalTransactions.stream().map(OriginalTransactionResponseDto::from).collect(Collectors.toList()));
    }

    @Transactional
    public OriginalTransactionResponseDto removeOriginalTransaction(Long id) {
        OriginalTransaction originalTransaction = originalTransactionRepository.findByIdOrElseThrow(id);

        originalTransaction.remove();
        originalTransactionRepository.save(originalTransaction);

        List<Transaction> transactions = transactionRepository.findTransactionsByOriginalTransaction(originalTransaction);
        transactionRepository.deleteAllInBatch(transactions);

        excelUploadUtil.deleteExcel(excelUploadUtil.getExcelKey(originalTransaction.getFileName(), originalTransaction.getUploadAt()));

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
            throw new ExcelUploadException(file.getOriginalFilename(), errors);
        }

        originalTransactionRepository.findOriginalTransactionByFileNameAndUploadAtAndIsRemovedFalse(fileName, uploadAt).ifPresent((originalTransaction) -> {
                    throw new BusinessException(ErrorCode.ORIGINAL_TRANSACTION_ALREADY_EXIST);
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

        String year = uploadAt.substring(0, 4);
        String month = uploadAt.substring(4, 6);

        try {
            emailSender.sendMail(DEV_TARGET_EMAIL, DEV_TARGET_EMAIL_TITLE, DEV_TARGET_EMAIL_NAME, year, month);
        } catch (MessagingException | IOException e) {
            log.error("메일 전송 실패 : {}", e.getMessage());
        }

        // TODO: 추후 메일 전송 로직을 다음 메서드로 변경
//        sendMailAfterUpload(year, month);
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
            throw new BusinessException(ErrorCode.ORIGINAL_TRANSACTION_NOT_READ_FROM_S3);
        }
    }

    // TODO: 메일 전송 로직을 다음 메서드로 변경
    private void sendMailAfterUpload(String year, String month) {
        List<Member> artists = memberRepository.findMemberByRoleAndIsRemovedFalse(MemberRole.ARTIST);
        artists.forEach(artist -> {
            try {
                emailSender.sendMail(artist.getEmail().getValue(), "Bluekey music 정산 알림",
                        artist.getName(), year, month);
            } catch (MessagingException | IOException e) {
                log.error("{}에게 메일 전송 실패 : {}", artist.getName(), e.getMessage());
            }
        });
    }
}
