package com.github.bluekey.processor.provider;

import com.github.bluekey.processor.ExcelRowException;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class ThreePointOneFourDistributorExcelProvider implements ExcelFileProvider{
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

    @Override
    public List<ExcelRowException> getErrors() {
        return null;
    }
}
