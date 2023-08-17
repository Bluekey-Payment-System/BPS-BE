package com.github.bluekey.processor.provider;

import com.github.bluekey.processor.ExcelRowException;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface ExcelFileProvider {
    Sheet getActiveSheet();

    void process(Sheet sheet);

    List<ExcelRowException> getErrors();

    List<ExcelRowException> getWarnings();
}
