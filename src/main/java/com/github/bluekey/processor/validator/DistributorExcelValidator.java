package com.github.bluekey.processor.validator;

import com.github.bluekey.processor.ExcelRowException;
import com.github.bluekey.processor.type.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DistributorExcelValidator implements ExcelValidator {
    private static final int ROW_DIFFERENCE_ADJUSTMENT = 1;

    @Override
    public boolean hasCellNullValue(Cell cell) {
        CellType cellType = cell.getCellType();

        if (cellType.equals(CellType.STRING)) {
            return cell.getStringCellValue() == null;
        }

        return cellType.equals(CellType.BLANK);
    }

    @Override
    public boolean hasInValidColumns(Row row, String distributorType) {
        for(Cell cell: row) {
            int index = cell.getColumnIndex();
            String value = cell.getStringCellValue();
            if (distributorType.equals("ATO")) {
                ColumnType columnType = AtoExcelColumnType.valueOfIndex(index);
                if (!columnType.getColumnName().equals(value)) {
                    return true;
                }
            }
            if (distributorType.equals("3.14")) {
                ColumnType columnType = ThreePointOneFourExcelColumnType.valueOfIndex(index);
                if (!columnType.getColumnName().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ExcelRowException generateException(ColumnType columnType, ExcelRowExceptionType type, Cell cell, int rowIndex) {
        String columnValue = getCellValueAsString(cell);
        return ExcelRowException.builder()
                .rowIndex(rowIndex + ROW_DIFFERENCE_ADJUSTMENT)
                .columnIndex(cell.getColumnIndex())
                .columnName(columnType.getColumnName())
                .type(type)
                .cellValue(columnValue)
                .build();

    }

    @Override
    public boolean hasValidSheetName(Workbook workbook, String sheetName, int sheetIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        return sheet.getSheetName().equals(sheetName);
    }

    public boolean hasCellZeroValue(Cell cell) {
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
