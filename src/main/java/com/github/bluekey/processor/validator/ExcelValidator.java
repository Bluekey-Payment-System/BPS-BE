package com.github.bluekey.processor.validator;

import com.github.bluekey.processor.ExcelRowException;
import com.github.bluekey.processor.type.ColumnType;
import com.github.bluekey.processor.type.ExcelRowExceptionType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelValidator {

    boolean hasCellNullValue(Cell cell);

    boolean hasInValidColumns(Row row);

    boolean hasValidSheetName(Workbook workbook);

    ExcelRowException generateException(ColumnType columnType, ExcelRowExceptionType type, Cell cell, int rowIndex);
}
