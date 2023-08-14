package com.github.bluekey.processor.provider;

import org.apache.poi.ss.usermodel.Sheet;

public class ThreePointOneFourDistributorExcelProvider implements ExcelFileProvider{
    @Override
    public Sheet getActiveSheet() {
        return null;
    }

    @Override
    public void process() {

    }

    @Override
    public boolean hasValidSheetName() {
        return false;
    }
}
