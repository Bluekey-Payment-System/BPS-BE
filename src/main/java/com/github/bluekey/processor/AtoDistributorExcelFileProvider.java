package com.github.bluekey.processor;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class AtoDistributorExcelFileProvider implements ExcelFileProvider {
    private static final int ACTIVE_EXCEL_SHEET_INDEX = 1;
    private static final String ACTIVE_EXCEL_SHEET_NAME = "전체매출내역";
    private final MultipartFile file;

    public AtoDistributorExcelFileProvider(MultipartFile file) {
        this.file = file;
    }

    @Override
    public Sheet getActiveSheet() {
        return null;
    }
}
