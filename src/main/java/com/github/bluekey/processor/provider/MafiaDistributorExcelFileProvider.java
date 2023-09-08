package com.github.bluekey.processor.provider;

import com.github.bluekey.processor.ExcelRowException;
import com.github.bluekey.processor.validator.DBPersistenceValidator;
import com.github.bluekey.processor.validator.DistributorExcelValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.github.bluekey.processor.type.AtoExcelColumnType.ALBUM_NAME;
import static com.github.bluekey.processor.type.AtoExcelColumnType.TRACK_NAME;
import static com.github.bluekey.processor.type.ExcelRowExceptionType.*;
import static com.github.bluekey.processor.type.MafiaExcelColumnType.*;

@Slf4j
@Getter
@RequiredArgsConstructor
public class MafiaDistributorExcelFileProvider implements ExcelFileProvider {

    private static final int ACTIVE_EXCEL_SHEET_INDEX = 0;
    private static final String SHEET_NAME = "Sheet1";
    private static final int MIN_COLUMN_INDEX = 4;
    private static final int MAX_COLUMN_INDEX = 15;
    private static final int HEADER_ROW_INDEX = 2;
    private static final int DATA_ROW_START_INDEX = 3;
    private final List<ExcelRowException> errorRows = new ArrayList<>();
    private final List<ExcelRowException> warningRows = new ArrayList<>();
    private final DistributorExcelValidator distributorCellValidator;

    private Workbook workbook;
    private String artistName;
    private String uploadAt;
    private int activeColumnIndex;
    private final DBPersistenceValidator dbPersistenceValidator;

    public MafiaDistributorExcelFileProvider(
            MultipartFile file,
            DBPersistenceValidator dbPersistenceValidator,
            String fileName
    ) {
        this.workbook = setWorkBook(file);
        this.distributorCellValidator = new DistributorExcelValidator();
        this.dbPersistenceValidator = dbPersistenceValidator;
        this.artistName = validateArtistName(fileName);
        this.uploadAt = getUploadAt(fileName);
    }

    @Override
    public Sheet getActiveSheet() {
        if (distributorCellValidator.hasValidSheetName(workbook, SHEET_NAME, ACTIVE_EXCEL_SHEET_INDEX)) {
            return workbook.getSheetAt(ACTIVE_EXCEL_SHEET_INDEX);
        }
        throw new RuntimeException("Invalid sheet name");
    }

    @Override
    public void process(Sheet sheet) {
        for (int i = DATA_ROW_START_INDEX; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            processCell(row);
        }
    }

    @Override
    public List<ExcelRowException> getErrors() {
        return this.errorRows;
    }

    @Override
    public List<ExcelRowException> getWarnings() {
        return this.warningRows;
    }

    private Workbook setWorkBook(MultipartFile file) {
        try {
            return WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing excel file");
        }
    }

    private String validateArtistName(String fileName) {
        String artistName = fileName.split("_")[2];
        if (dbPersistenceValidator.hasNotExistArtistWithExcelName(artistName)) {
            throw new RuntimeException("Artist not exist.");
        }
        return artistName;
    }

    private String getUploadAt(String fileName) {
        return fileName.split("_")[1];
    }

    private boolean isActiveUploadAt(Double uploadAtInExcel) {
        Date date = DateUtil.getJavaDate(uploadAtInExcel);
        LocalDate localDateInExcel = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate = LocalDate.parse(uploadAt+"01", DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("uploadAt = {} {} {}", localDate, localDateInExcel, localDate.equals(localDateInExcel));
        if(localDate.equals(localDateInExcel)) {
            return true;
        }
        return false;
    }

    private void processCell(Row row) {
        for (Cell cell : row) {
            validateCell(cell, row);
        }
    }

    private void validateCell(Cell cell, Row row) {
        if (ALBUM_NAME.getIndex() == cell.getColumnIndex()) {
//            validateAlbumNameCell(cell, row);
        }
        if (TRACK_NAME.getIndex() == cell.getColumnIndex()) {
//            validateTrackNameCell(cell, row);
        }
    }

    private void validateAlbumNameCell(Cell cell, Row row) {
        if (!distributorCellValidator.hasCellNullValue(cell)) {
            if (dbPersistenceValidator.hasNotExistedAlbum(cell)) {
                ExcelRowException excelRowException = distributorCellValidator.generateException(ALBUM_NAME, NOT_EXIST, cell, row.getRowNum());
                errorRows.add(excelRowException);
            }
        }
    }

    private void validateTrackNameCell(Cell cell, Row row) {
        // 엑셀파일에서 트랙명이 null인 경우
        if (!distributorCellValidator.hasCellNullValue(cell)) {
            if (dbPersistenceValidator.hasNotExistedTrack(cell, row.getCell(ALBUM_NAME.getIndex()))) {
                ExcelRowException excelRowException = distributorCellValidator.generateException(TRACK_NAME, NOT_EXIST, cell, row.getRowNum());
                errorRows.add(excelRowException);
            }
        }
    }
}
