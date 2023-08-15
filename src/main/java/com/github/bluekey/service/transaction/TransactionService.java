package com.github.bluekey.service.transaction;

import com.github.bluekey.dto.request.transaction.OriginalTransactionRequestDto;
import com.github.bluekey.dto.response.ListResponse;
import com.github.bluekey.dto.response.transaction.OriginalTransactionResponseDto;
import com.github.bluekey.entity.transaction.OriginalTransaction;
import com.github.bluekey.exception.transaction.ExcelUploadException;
import com.github.bluekey.processor.ExcelFileProcessManager;
import com.github.bluekey.processor.ExcelRowException;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.transaction.OriginalTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private static final int ERROR_THRESHOLD = 0;
    private final OriginalTransactionRepository originalTransactionRepository;
    private final MemberRepository memberRepository;

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
        ExcelFileProcessManager excelFileProcessManager = new ExcelFileProcessManager(file, memberRepository);
        excelFileProcessManager.process();
        List<ExcelRowException> errors = excelFileProcessManager.getErrors();
        if (errors.size() > ERROR_THRESHOLD) {
            throw new ExcelUploadException(errors);
        }

        OriginalTransaction originalTransaction = OriginalTransaction.builder()
                .uploadAt(requestDto.getUploadAt())
                .fileName(file.getOriginalFilename())
                .build();
        originalTransactionRepository.save(originalTransaction);
        return OriginalTransactionResponseDto.from(originalTransaction);
    }
}
