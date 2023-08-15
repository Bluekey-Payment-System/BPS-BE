package com.github.bluekey.processor.provider;

import org.apache.poi.ss.usermodel.Sheet;

public class MafiaDistributorExcelFileProvider implements ExcelFileProvider{
    @Override
    public Sheet getActiveSheet() {
        return null;
    }

    @Override
    public void process(Sheet sheet) {

    }

    @Override
    public boolean hasValidSheetName() {
        return false;
    }
}
