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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OriginalTransactionPaginationResponseDto.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<ListResponse<OriginalTransactionResponseDto>> getOriginalTransactionHistory(
            @Parameter(description = "엑셀파일이 업로드된 날짜 (format: yyyy-MM)") @RequestParam String monthly
            ) {
        return ResponseEntity.ok(transactionService.getOriginalTransactions(monthly));
    }

    @Operation(summary = "엑셀 파일 업로드 History 삭제 API", description = "관리자가 정상적으로 업로드가 완료된 엑셀파일 내역을 삭제할 수 있는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "리스트 형태로 정상적으로 업로드가 완료된 엑셀 파일 내역중 해당 pk에 해당하는 인스턴스를 삭제하고 반환합니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OriginalTransactionResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<OriginalTransactionResponseDto> removeOriginalTransactionHistory(
            @Parameter(description = "삭제할 엑셀 파일 pk") @PathVariable Long id
    ) {
        return ResponseEntity.ok(transactionService.removeOriginalTransaction(id));
    }

    @Operation(summary = "엑셀 파일 업로드 API", description = "관리자가 정상적으로 엑셀파일을 업로드할 수 있는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "엑셀 파일이 정상적으로 업로드 되고 업로드 된 파일의 정보를 반환합니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OriginalTransactionResponseDto.class)
                    )
            )
    })
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<OriginalTransactionResponseDto> saveOriginalTransactionHistory(
//            @Parameter(description = "multipart/form-data 형식의 엑셀 파일 데이터, key 값은 file 입니다.")
//            @RequestParam("file") MultipartFile file
//    ) {
//        return ResponseEntity.ok(transactionService.saveOriginalTransaction(file));
//    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveOriginalTransactionHistory(
            @Parameter(description = "multipart/form-data 형식의 엑셀 파일 데이터, key 값은 file 입니다.")
            @RequestParam("file") MultipartFile file
    ) {
        transactionService.saveOriginalTransaction(file);
        return ResponseEntity.ok().build();
    }
}
