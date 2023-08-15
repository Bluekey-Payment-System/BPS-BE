package com.github.bluekey.processor.provider;

import com.github.bluekey.processor.type.AtoExcelColumnType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class AtoDistributorExcelFileProvider implements ExcelFileProvider {
    private static final int ACTIVE_EXCEL_SHEET_INDEX = 1;
    private static final String ACTIVE_EXCEL_SHEET_NAME = "전체매출내역";
    private static final int MIN_COLUMN_INDEX = 0;
    private static final int MAX_COLUMN_INDEX = 10;
    private static final int HEADER_ROW_INDEX = 3;
    private static final int DATA_ROW_START_INDEX = 5;
    private final Map<Integer, String> errorRows = new LinkedHashMap<>();

    private final Workbook workbook;

    public AtoDistributorExcelFileProvider(MultipartFile file) {
        this.workbook = setWorkBook(file);
    }

    @Override
    public Sheet getActiveSheet() {
        if (hasValidSheetName()) {
            return workbook.getSheetAt(ACTIVE_EXCEL_SHEET_INDEX);
        }
        throw new RuntimeException("Invalid sheet name");
    }

    @Override
    public void process(Sheet sheet) {
        for (int i = DATA_ROW_START_INDEX; i<= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            processCell(row);
        }
        System.out.println(errorRows);
    }

    @Override
    public boolean hasValidSheetName() {
        Sheet sheet = workbook.getSheetAt(ACTIVE_EXCEL_SHEET_INDEX);
        return sheet.getSheetName().equals(ACTIVE_EXCEL_SHEET_NAME);
    }

    private Workbook setWorkBook(MultipartFile file) {
        try {
            return WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing excel file");
        }
    }

    private void processCell(Row row) {
        for (Cell cell: row) {
            validateCell(cell, row.getRowNum());
        }
    }

    private void validateCell(Cell cell, int index) {
        if (AtoExcelColumnType.ARTIST_NAME.getIndex() == cell.getColumnIndex() && cell.getStringCellValue() == null) {
            errorRows.put(index, AtoExcelColumnType.ARTIST_NAME.getColumnName());
            System.out.println(cell.getStringCellValue());
        }
        if (AtoExcelColumnType.ALBUM_NAME.getIndex() == cell.getColumnIndex() && cell.getCellType() == CellType.NUMERIC) {
            if (cell.getNumericCellValue() == 0) {
                errorRows.put(index, AtoExcelColumnType.ALBUM_NAME.getColumnName());
            }
        }
    }
}
