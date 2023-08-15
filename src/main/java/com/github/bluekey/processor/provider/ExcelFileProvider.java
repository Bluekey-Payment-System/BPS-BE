package com.github.bluekey.processor.provider;

import org.apache.poi.ss.usermodel.Sheet;

public interface ExcelFileProvider {
    Sheet getActiveSheet();

    void process(Sheet sheet);

    boolean hasValidSheetName();
}
