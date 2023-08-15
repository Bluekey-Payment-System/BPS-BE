package com.github.bluekey.processor.provider;

import com.github.bluekey.processor.ExcelRowException;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Map;

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

    @Override
    public Map<Integer, ExcelRowException> getErrors() {
        return null;
    }
}
