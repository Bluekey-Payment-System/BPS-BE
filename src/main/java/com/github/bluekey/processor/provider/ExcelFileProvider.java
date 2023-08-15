package com.github.bluekey.processor.provider;

import com.github.bluekey.processor.ExcelRowException;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Map;

public interface ExcelFileProvider {
    Sheet getActiveSheet();

    void process(Sheet sheet);

    boolean hasValidSheetName();

    Map<Integer, ExcelRowException> getErrors();
}
