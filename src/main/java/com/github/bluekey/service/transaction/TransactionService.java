package com.github.bluekey.service.transaction;

import com.github.bluekey.dto.response.ListResponse;
import com.github.bluekey.dto.response.transaction.OriginalTransactionResponseDto;
import com.github.bluekey.entity.transaction.OriginalTransaction;
import com.github.bluekey.repository.transaction.OriginalTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final OriginalTransactionRepository originalTransactionRepository;

    public ListResponse<OriginalTransactionResponseDto> getOriginalTransactions(String uploadAt) {
        List<OriginalTransaction> originalTransactions = originalTransactionRepository.findAllByUploadAt(uploadAt);
        return new ListResponse<>(originalTransactions.size(), originalTransactions.stream().map(OriginalTransactionResponseDto::from).collect(Collectors.toList()));
    }

    public OriginalTransactionResponseDto removeOriginalTransaction(Long id) {
        OriginalTransaction originalTransaction = originalTransactionRepository.findByIdOrElseThrow(id);

        // 삭제 로직

        return OriginalTransactionResponseDto.from(originalTransaction);
    }

    public OriginalTransactionResponseDto saveOriginalTransaction() {
        // 임시 코드
        Long id = 1L;
        OriginalTransaction originalTransaction = originalTransactionRepository.findByIdOrElseThrow(id);

        // validation logic

        return OriginalTransactionResponseDto.from(originalTransaction);
    }
}
