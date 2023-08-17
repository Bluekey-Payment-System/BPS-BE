package com.github.bluekey.processor.validator;

import org.apache.poi.ss.usermodel.Cell;

public interface ExcelValidator<T> {

    boolean hasCellNullValue(T columnType, Cell cell);
}
