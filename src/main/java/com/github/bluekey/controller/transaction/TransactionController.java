package com.github.bluekey.controller.transaction;

import com.github.bluekey.dto.response.ListResponse;
import com.github.bluekey.dto.response.transaction.OriginalTransactionPaginationResponseDto;
import com.github.bluekey.dto.response.transaction.OriginalTransactionResponseDto;
import com.github.bluekey.service.transaction.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transaction", description = "엑셀 파일 History 관리와 업로드 API Document")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "엑셀 파일 업로드 History 조회 API", description = "관리자가 정상적으로 업로드가 완료된 엑셀파일 내역을 조회할 수 있는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "리스트 형태로 정상적으로 업로드가 완료된 엑셀 파일 내역을 반환합니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OriginalTransactionPaginationResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<ListResponse<OriginalTransactionResponseDto>> getOriginalTransactionHistory(
            @Parameter(description = "엑셀파일이 업로드된 날짜 (format: yyyy-MM)") @RequestParam String monthly
            ) {
        return ResponseEntity.ok(transactionService.getOriginalTransactions(monthly));
    }
}
