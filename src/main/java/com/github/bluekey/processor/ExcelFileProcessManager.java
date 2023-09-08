package com.github.bluekey.processor;

import com.github.bluekey.entity.transaction.ExcelDistributorType;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.processor.provider.AtoDistributorExcelFileProvider;
import com.github.bluekey.processor.provider.ExcelFileProvider;
import com.github.bluekey.processor.provider.MafiaDistributorExcelFileProvider;
import com.github.bluekey.processor.provider.ThreePointOneFourDistributorExcelFileProvider;
import com.github.bluekey.processor.type.MusicDistributorType;
import com.github.bluekey.processor.validator.DBPersistenceValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
@Component
public class ExcelFileProcessManager implements ProcessManager {
    private static final String FILE_SEPARATOR = "\\.";
    private final DBPersistenceValidator dbPersistenceValidator;

    private MultipartFile file;
    private ExcelFileProvider excelFileProvider;
    private String filetype;
    private String fileName;
    private ExcelDistributorType distributorType;

    @Override
    public void process() {
        excelFileProvider.process(excelFileProvider.getActiveSheet());
    }

    public List<ExcelRowException> getErrors() {
        return excelFileProvider.getErrors();
    }

    public List<ExcelRowException> getWarnings() {
        return excelFileProvider.getWarnings();
    }

    public ExcelDistributorType getDistributorType() {
        return this.distributorType;
    }

    public void setExcelFileBasicInformation(MultipartFile file) {
        String[] originalFileNameInformation = file.getOriginalFilename().split(FILE_SEPARATOR);
        this.fileName = originalFileNameInformation[0];
        this.filetype = originalFileNameInformation[1];
        this.file = file;
        this.excelFileProvider = setProvider();
    }

    private ExcelFileProvider setProvider() {
        for (MusicDistributorType type : MusicDistributorType.values()) {
            Pattern pattern = Pattern.compile(type.getPattern());
            Matcher matcher = pattern.matcher(fileName);
            if (matcher.matches()) {
                return determineExcelFileProviderWithMusicDistributorType(type);
            }
        }
        throw new BusinessException(ErrorCode.TRANSACTION_INVALID_EXCEL_FILE_NAME);
    }

    private ExcelFileProvider determineExcelFileProviderWithMusicDistributorType(MusicDistributorType type) {
        if (type.getCls().equals(AtoDistributorExcelFileProvider.class)) {
            this.distributorType = ExcelDistributorType.ATO;
            return new AtoDistributorExcelFileProvider(file, dbPersistenceValidator);
        }
        if (type.getCls().equals(ThreePointOneFourDistributorExcelFileProvider.class)) {
            this.distributorType = ExcelDistributorType.THREE_POINT_ONE_FOUR;
            return new ThreePointOneFourDistributorExcelFileProvider(file, dbPersistenceValidator);
        }
        if (type.getCls().equals(MafiaDistributorExcelFileProvider.class)) {
            this.distributorType = ExcelDistributorType.MAFIA;
            return new MafiaDistributorExcelFileProvider(file, dbPersistenceValidator, fileName);
        }

        throw new BusinessException(ErrorCode.ORIGINAL_TRANSACTION_INVALID_EXCEL_FILE_TYPE);
    }
}
