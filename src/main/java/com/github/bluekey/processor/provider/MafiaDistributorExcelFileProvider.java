package com.github.bluekey.processor.provider;

import com.github.bluekey.processor.ExcelRowException;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MafiaDistributorExcelFileProvider implements ExcelFileProvider{
    @Override
    public Sheet getActiveSheet() {
        return null;
    }

    @Override
    public void process(Sheet sheet) {

    }

    @Override
    public List<ExcelRowException> getErrors() {
        return null;
    }
}
