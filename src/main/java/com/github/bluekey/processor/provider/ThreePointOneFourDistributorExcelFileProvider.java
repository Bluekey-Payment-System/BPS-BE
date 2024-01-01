package com.github.bluekey.processor.provider;

import com.github.bluekey.processor.ExcelRowException;
import com.github.bluekey.processor.validator.DistributorExcelValidator;
import com.github.bluekey.processor.validator.DBPersistenceValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.bluekey.processor.type.ThreePointOneFourExcelColumnType.*;
import static com.github.bluekey.processor.type.ExcelRowExceptionType.*;
import static com.github.bluekey.processor.type.ExcelRowExceptionType.NOT_EXIST;

@Getter
@RequiredArgsConstructor
public class ThreePointOneFourDistributorExcelFileProvider implements ExcelFileProvider {
    private static final int ACTIVE_EXCEL_SHEET_INDEX = 2;
    private static final String SHEET_NAME = "상세내역";
    private static final int MIN_COLUMN_INDEX = 0;
    private static final int MAX_COLUMN_INDEX = 13;
    private static final int HEADER_ROW_INDEX = 3;
    private static final int DATA_ROW_START_INDEX = 4;
    private final List<ExcelRowException> errorRows = new ArrayList<>();
    private final List<ExcelRowException> warningRows = new ArrayList<>();
    private final DistributorExcelValidator atoDistributorCellValidator;
    private Workbook workbook;
    private final DBPersistenceValidator dbPersistenceValidator;

    public ThreePointOneFourDistributorExcelFileProvider(
            MultipartFile file,
            DBPersistenceValidator dbPersistenceValidator
    ) {
        this.workbook = setWorkBook(file);
        this.atoDistributorCellValidator = new DistributorExcelValidator();
        this.dbPersistenceValidator = dbPersistenceValidator;
    }

    @Override
    public Sheet getActiveSheet() {
        if (atoDistributorCellValidator.hasValidSheetName(workbook, SHEET_NAME, ACTIVE_EXCEL_SHEET_INDEX)) {
            return workbook.getSheetAt(ACTIVE_EXCEL_SHEET_INDEX);
        }
        throw new RuntimeException("Invalid sheet name");
    }

    @Override
    public void process(Sheet sheet) {
        if (atoDistributorCellValidator.hasInValidColumns(sheet.getRow(HEADER_ROW_INDEX), "3.14")) {
            throw new RuntimeException("Invalid columns definition");
        }
        for (int i = DATA_ROW_START_INDEX; i<= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            processCell(row);
        }
    }

    @Override
    public List<ExcelRowException> getErrors() {
        return errorRows;
    }

    @Override
    public List<ExcelRowException> getWarnings() {
        return warningRows;
    }

    private Workbook setWorkBook(MultipartFile file) {
        try {
            return WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing excel file");
        }
    }

    private void processCell(Row row) {
        for (Cell cell: row) {
            validateCell(cell, row);
        }
    }

    private void validateCell(Cell cell, Row row) {
        if (ARTIST_NAME.getIndex() == cell.getColumnIndex()) {
            validateArtistNameCell(cell, row);
        }
        if (ALBUM_NAME.getIndex() == cell.getColumnIndex()) {
            validateAlbumNameCell(cell, row);
        }
        if (TRACK_NAME.getIndex() == cell.getColumnIndex()) {
            validateTrackNameCell(cell, row);
        }
    }

    private void validateArtistNameCell(Cell cell, Row row) {
        // 엑셀파일에서 아티스트명이 null인 경우
        if (atoDistributorCellValidator.hasCellNullValue(cell)) {
            ExcelRowException excelRowException = atoDistributorCellValidator.generateException(ARTIST_NAME, NULL_CELL, cell, row.getRowNum() + DATA_ROW_START_INDEX);
            errorRows.add(excelRowException);
        }

        // 가창자의 경우 Artist에도 없고 TrackMember에도 없을 경우 exception 반환
        if (dbPersistenceValidator.hasNotExistedArtist(cell)) {
            if(dbPersistenceValidator.hasNotExistedTrackMember(cell, row.getCell(TRACK_NAME.getIndex()), row.getCell(ALBUM_NAME.getIndex()))) {
                ExcelRowException excelRowException = atoDistributorCellValidator.generateException(ARTIST_NAME, NOT_EXIST_ARTIST_NAME, cell, row.getRowNum() + DATA_ROW_START_INDEX);
                errorRows.add(excelRowException);
            }
        }
    }

    private void validateAlbumNameCell(Cell cell, Row row) {
        // 엑셀파일에서 앨범명이 null인 경우
        if (atoDistributorCellValidator.hasCellNullValue(cell)) {
            ExcelRowException excelRowException = atoDistributorCellValidator.generateException(ALBUM_NAME, NULL_CELL, cell, row.getRowNum() + DATA_ROW_START_INDEX);
            errorRows.add(excelRowException);
        }
    }

    private void validateTrackNameCell(Cell cell, Row row) {
        // 엑셀파일에서 트랙명이 null인 경우
        if (atoDistributorCellValidator.hasCellNullValue(cell)) {
            ExcelRowException excelRowException = atoDistributorCellValidator.generateException(TRACK_NAME, NULL_CELL, cell, row.getRowNum() + DATA_ROW_START_INDEX);
            errorRows.add(excelRowException);
        }

        if (dbPersistenceValidator.hasNotExistedTrack(cell, row.getCell(ALBUM_NAME.getIndex()))) {
            ExcelRowException excelRowException = atoDistributorCellValidator.generateException(TRACK_NAME, NOT_EXIST_TRACK_NAME, cell, row.getRowNum() + DATA_ROW_START_INDEX);
            errorRows.add(excelRowException);
        }
    }
}
