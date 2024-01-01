package com.github.bluekey.processor.provider;

import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.processor.ExcelRowException;

import com.github.bluekey.processor.validator.DistributorExcelValidator;
import com.github.bluekey.processor.validator.DBPersistenceValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.github.bluekey.processor.type.AtoExcelColumnType.*;
import static com.github.bluekey.processor.type.ExcelRowExceptionType.*;

@Getter
@RequiredArgsConstructor
public class AtoDistributorExcelFileProvider implements ExcelFileProvider {
    private static final int ACTIVE_EXCEL_SHEET_INDEX = 1;
    private static final String SHEET_NAME = "전체매출내역";
    private static final String ALLOW_EXCEPTION_SERVICE_NAME_THRESHOLD = "유튜브";
    private static final int MIN_COLUMN_INDEX = 0;
    private static final int MAX_COLUMN_INDEX = 10;
    private static final int HEADER_ROW_INDEX = 3;
    private static final int DATA_ROW_START_INDEX = 5;
    private final List<ExcelRowException> errorRows = new ArrayList<>();
    private final List<ExcelRowException> warningRows = new ArrayList<>();
    private final DistributorExcelValidator atoDistributorCellValidator;

    private Workbook workbook;
    private final DBPersistenceValidator dbPersistenceValidator;

    public AtoDistributorExcelFileProvider(
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
        throw new BusinessException(ErrorCode.EXCEL_INVALID_SHEET_NAME);
    }

    @Override
    public void process(Sheet sheet) {
        if (atoDistributorCellValidator.hasInValidColumns(sheet.getRow(HEADER_ROW_INDEX), "ATO")) {
            throw new BusinessException(ErrorCode.EXCEL_INVALID_COLUMN_DEFINITION);
        }
        for (int i = DATA_ROW_START_INDEX; i <= sheet.getLastRowNum(); i++) {
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

    public void updateWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    private Workbook setWorkBook(MultipartFile file) {
        try {
            return WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.EXCEL_NOT_CONVERT_TO_WORKBOOK);
        }
    }

    private void processCell(Row row) {
        for (Cell cell : row) {
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
            ExcelRowException excelRowException = atoDistributorCellValidator.generateException(ARTIST_NAME, NULL_CELL, cell, row.getRowNum());
            errorRows.add(excelRowException);
        }

        // 가창자의 경우 Artist에도 없고 TrackMember에도 없을 경우 exception 반환
        if (dbPersistenceValidator.hasNotExistedArtist(cell)) {
            if (dbPersistenceValidator.hasNotExistedTrackMember(cell, row.getCell(TRACK_NAME.getIndex()), row.getCell(ALBUM_NAME.getIndex()))) {
                ExcelRowException excelRowException = atoDistributorCellValidator.generateException(ARTIST_NAME, NOT_EXIST_ARTIST_NAME, cell, row.getRowNum());
                errorRows.add(excelRowException);
            }
        }
    }

    private void validateAlbumNameCell(Cell cell, Row row) {
        // 엑셀파일에서 앨범명이 null인 경우
        if (atoDistributorCellValidator.hasCellNullValue(cell)) {
            ExcelRowException excelRowException = atoDistributorCellValidator.generateException(ALBUM_NAME, NULL_CELL, cell, row.getRowNum());
            errorRows.add(excelRowException);
        }

        // 앨범 값이 0인 경우
        if (isAlbumExceptionAllowCase(cell, row)) {
            ExcelRowException excelRowException = atoDistributorCellValidator.generateException(ALBUM_NAME, ALLOW_EXCEPTION_CASE, cell, row.getRowNum());
            warningRows.add(excelRowException);
            Cell cellTrack = row.getCell(TRACK_NAME.getIndex());

            // TODO: 앨범명을 특정할 수 없을 때 track이 서로 다른 앨범에 종속되어 있는 경우
//            if (dbPersistenceValidator.hasDuplicatedTrack(cellTrack)) {
//                ExcelRowException excelRowExceptionByYouTube = atoDistributorCellValidator.generateException(TRACK_NAME, DUPLICATED_TRACKS_BY_YOUTUBE, cellTrack, row.getRowNum());
//                errorRows.add(excelRowExceptionByYouTube);
//            }
        }

        if (!isAlbumExceptionAllowCase(cell, row) && dbPersistenceValidator.hasNotExistedAlbum(cell) && !atoDistributorCellValidator.hasCellNullValue(cell)) {
            ExcelRowException excelRowException = atoDistributorCellValidator.generateException(ALBUM_NAME, NOT_EXIST_ALBUM_NAME, cell, row.getRowNum());
            errorRows.add(excelRowException);
        }
    }

    private void validateTrackNameCell(Cell cell, Row row) {
        // 엑셀파일에서 트랙명이 null인 경우
        if (atoDistributorCellValidator.hasCellNullValue(cell)) {
            ExcelRowException excelRowException = atoDistributorCellValidator.generateException(TRACK_NAME, NULL_CELL, cell, row.getRowNum());
            errorRows.add(excelRowException);
        }

        if (dbPersistenceValidator.hasNotExistedTrack(cell, row.getCell(ALBUM_NAME.getIndex()))) {

            if (!isAlbumExceptionAllowCaseInTrack(row)) {
                ExcelRowException excelRowException = atoDistributorCellValidator.generateException(TRACK_NAME, NOT_EXIST_TRACK_NAME, cell, row.getRowNum());
                errorRows.add(excelRowException);
            }
        }
    }

    private boolean isAlbumExceptionAllowCaseInTrack(Row row) {
        Cell cellAlbum = row.getCell(ALBUM_NAME.getIndex());
        Cell cellArtist = row.getCell(ARTIST_NAME.getIndex());
        Cell cellTrack = row.getCell(TRACK_NAME.getIndex());
        Cell cellServiceName = row.getCell(SERVICE_NAME.getIndex());

        // 아티스트명, 트랙명이 존재하고, 서비스명이 유튜브일 경우 경고 데이터로 종속
        if (!atoDistributorCellValidator.hasCellNullValue(cellArtist) &&
                !atoDistributorCellValidator.hasCellNullValue(cellTrack) &&
                atoDistributorCellValidator.hasCellZeroValue(cellAlbum) &&
                cellServiceName.getStringCellValue().equals(ALLOW_EXCEPTION_SERVICE_NAME_THRESHOLD)
        ) {
            return true;
        }
        return false;

    }

    private boolean isAlbumExceptionAllowCase(Cell cell, Row row) {
        if (atoDistributorCellValidator.hasCellZeroValue(cell)) {
            Cell cellArtist = row.getCell(ARTIST_NAME.getIndex());
            Cell cellTrack = row.getCell(TRACK_NAME.getIndex());
            Cell cellServiceName = row.getCell(SERVICE_NAME.getIndex());

            // 아티스트명, 트랙명이 존재하고, 서비스명이 유튜브일 경우 경고 데이터로 종속
            if (!atoDistributorCellValidator.hasCellNullValue(cellArtist) &&
                    !atoDistributorCellValidator.hasCellNullValue(cellTrack) &&
                    cellServiceName.getStringCellValue().equals(ALLOW_EXCEPTION_SERVICE_NAME_THRESHOLD)
            ) {
                return true;
            }
            return false;
        }
        return false;
    }
}
