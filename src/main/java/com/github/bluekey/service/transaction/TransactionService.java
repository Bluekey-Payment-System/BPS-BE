package com.github.bluekey.service.transaction;

import com.github.bluekey.dto.response.ListResponse;
import com.github.bluekey.dto.response.transaction.OriginalTransactionResponseDto;
import com.github.bluekey.entity.transaction.OriginalTransaction;
import com.github.bluekey.processor.ExcelFileProcessManager;
import com.github.bluekey.repository.transaction.OriginalTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public void saveOriginalTransaction(MultipartFile file) {
        ExcelFileProcessManager excelFileProcessManager = new ExcelFileProcessManager(file);
        excelFileProcessManager.process();
        // 임시 코드
//        Long id = 1L;
//        OriginalTransaction originalTransaction = originalTransactionRepository.findByIdOrElseThrow(id);

        // validation logic
//        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
//            int numberOfSheets = workbook.getNumberOfSheets();
//            for (int sheetIndex = 1; sheetIndex < numberOfSheets; sheetIndex++) {
//                Sheet sheet = workbook.getSheetAt(sheetIndex);
//                System.out.println(sheet.getSheetName());
//                for(int i = 0; i<50; i++) {
//                    Row row = sheet.getRow(i);
//                    for(int j = 0; j<50; j++) {
//                        Cell cell = row.getCell(j);
//                        System.out.println(row.getRowNum() + " " + cell);
//                    }
//
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Error processing excel file");
//        }

//        return OriginalTransactionResponseDto.from(originalTransaction);
    }
}
