package com.github.bluekey.processor;

import com.github.bluekey.processor.provider.AtoDistributorExcelFileProvider;
import com.github.bluekey.processor.provider.ExcelFileProvider;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ExcelFileProcessManager {
    private static final String FILE_SEPARATOR = ".";
    private final MultipartFile file;
    private final ExcelFileProvider excelFileProvider;
    private String filetype;
    private String fileName;

    public ExcelFileProcessManager(MultipartFile file) {
        setExcelFileBasicInformation(file);
        this.excelFileProvider = setProvider();
        this.file = file;
    }

    private void setExcelFileBasicInformation(MultipartFile file) {
        String[] originalFileNameInformation = file.getOriginalFilename().split(FILE_SEPARATOR);
        this.filetype = originalFileNameInformation[0];
        this.fileName = originalFileNameInformation[1];
    }

    private ExcelFileProvider setProvider() {
        for (MusicDistributorType type : MusicDistributorType.values()) {
            Pattern pattern = Pattern.compile(type.getPattern());
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.matches()) {
                return determineExcelFileProviderWithMusicDistributorType(type);
            }
        }
        throw new IllegalArgumentException("Unsupported file");
    }

    private ExcelFileProvider determineExcelFileProviderWithMusicDistributorType(MusicDistributorType type) {
        if (type.getCls().equals(AtoDistributorExcelFileProvider.class)) {
            return new AtoDistributorExcelFileProvider(file);
        }
        throw new IllegalArgumentException("Excel File Provider exception");
    }
}
