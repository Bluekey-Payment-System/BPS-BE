package com.github.bluekey.processor;

import org.apache.poi.ss.usermodel.Sheet;

public interface ExcelFileProvider {
    Sheet getActiveSheet();
}
