package com.github.bluekey.processor.validator;

import com.github.bluekey.processor.ExcelRowException;
import com.github.bluekey.processor.type.AtoExcelColumnType;
import com.github.bluekey.processor.type.ExcelRowExceptionType;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AtoDistributorExcelValidator implements ExcelValidator<AtoExcelColumnType> {
    private static final int ACTIVE_EXCEL_SHEET_INDEX = 1;
    private static final String ACTIVE_EXCEL_SHEET_NAME = "전체매출내역";

    @Override
    public boolean hasCellNullValue(Cell cell) {
        CellType cellType = cell.getCellType();

        if (cellType.equals(CellType.STRING)) {
            return cell.getStringCellValue() == null;
        }

        return cellType.equals(CellType.BLANK);
    }

    @Override
    public boolean hasInValidColumns(Row row) {
        for(Cell cell: row) {
            int index = cell.getColumnIndex();
            String value = cell.getStringCellValue();

            AtoExcelColumnType columnType = AtoExcelColumnType.valueOfIndex(index);
            if (!columnType.getColumnName().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ExcelRowException generateException(AtoExcelColumnType columnType, ExcelRowExceptionType type, Cell cell, int rowIndex) {
        String columnValue = getCellValueAsString(cell);
        return ExcelRowException.builder()
                .rowIndex(rowIndex + 1)
                .columnIndex(cell.getColumnIndex())
                .columnName(columnType.getColumnName())
                .type(type)
                .columnValue(columnValue)
                .build();

    }

    @Override
    public boolean hasValidSheetName(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(ACTIVE_EXCEL_SHEET_INDEX);
        return sheet.getSheetName().equals(ACTIVE_EXCEL_SHEET_NAME);
    }

    public boolean hasCellZeroValue(AtoExcelColumnType columnType, Cell cell) {
        CellType cellType = cell.getCellType();

        if(cellType.equals(CellType.NUMERIC)) {
            return cell.getNumericCellValue() == 0;
        }
        return false;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            return Double.toString(cell.getNumericCellValue());
        }
        return cell.getStringCellValue();
    }
}
