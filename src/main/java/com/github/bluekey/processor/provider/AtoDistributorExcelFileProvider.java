package com.github.bluekey.processor.provider;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.processor.ExcelRowException;
import com.github.bluekey.processor.NameExtractor;

import com.github.bluekey.processor.validator.AtoDistributorExcelValidator;
import com.github.bluekey.repository.member.MemberRepository;
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
    private static final String ALLOW_EXCEPTION_SERVICE_NAME_THRESHOLD = "유튜브";
    private static final int MIN_COLUMN_INDEX = 0;
    private static final int MAX_COLUMN_INDEX = 10;
    private static final int HEADER_ROW_INDEX = 3;
    private static final int DATA_ROW_START_INDEX = 5;
    private final List<ExcelRowException> errorRows = new ArrayList<>();
    private final List<ExcelRowException> warningRows = new ArrayList<>();
    private final AtoDistributorExcelValidator atoDistributorCellValidator;

    private final Workbook workbook;
    private final MemberRepository memberRepository;

    public AtoDistributorExcelFileProvider(MultipartFile file, MemberRepository memberRepository) {
        this.workbook = setWorkBook(file);
        this.memberRepository = memberRepository;
        this.atoDistributorCellValidator = new AtoDistributorExcelValidator();
    }

    @Override
    public Sheet getActiveSheet() {
        if (atoDistributorCellValidator.hasValidSheetName(workbook)) {
            return workbook.getSheetAt(ACTIVE_EXCEL_SHEET_INDEX);
        }
        throw new RuntimeException("Invalid sheet name");
    }

    @Override
    public void process(Sheet sheet) {
        if (atoDistributorCellValidator.hasInValidColumns(sheet.getRow(HEADER_ROW_INDEX))) {
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

    private void validateAlbumNameCell(Cell cell, Row row) {
        // 엑셀파일에서 앨범명이 null인 경우
        if (atoDistributorCellValidator.hasCellNullValue(cell)) {
            ExcelRowException excelRowException = atoDistributorCellValidator.generateException(ALBUM_NAME, NULL_CELL, cell, row.getRowNum());
            errorRows.add(excelRowException);
        }

        // 앨범 값이 0인 경우
        if (atoDistributorCellValidator.hasCellZeroValue(ALBUM_NAME, cell)) {
            Cell cellArtist = row.getCell(ARTIST_NAME.getIndex());
            Cell cellTrack = row.getCell(TRACK_NAME.getIndex());
            Cell cellServiceName = row.getCell(SERVICE_NAME.getIndex());

            // 아티스트명, 트랙명이 존재하고, 서비스명이 유튜브일 경우 경고 데이터로 종속
            if (!atoDistributorCellValidator.hasCellNullValue(cellArtist) &&
                    !atoDistributorCellValidator.hasCellNullValue(cellTrack) &&
                    cellServiceName.getStringCellValue().equals(ALLOW_EXCEPTION_SERVICE_NAME_THRESHOLD)
            ) {
                ExcelRowException excelRowException = atoDistributorCellValidator.generateException(ALBUM_NAME, ALLOW_EXCEPTION_CASE, cell, row.getRowNum());
                warningRows.add(excelRowException);
            }
        }
    }

    private void validateArtistNameCell(Cell cell, Row row) {
        // 엑셀파일에서 아티스트명이 null인 경우
        if (atoDistributorCellValidator.hasCellNullValue(cell)) {
            ExcelRowException excelRowException = atoDistributorCellValidator.generateException(ARTIST_NAME, NULL_CELL, cell, row.getRowNum());
            errorRows.add(excelRowException);
        }
        // Entity와 Repository가 도입되면 추가
//        if (cell.getCellType().equals(CellType.STRING)) {
//            if (hasNotExistedArtist(cell)) {
//                ExcelRowException excelRowException = ExcelRowException.builder()
//                        .rowIndex(index + 1)
//                        .columnIndex(cell.getColumnIndex())
//                        .columnName(ARTIST_NAME.getColumnName())
//                        .type(NOT_EXIST)
//                        .columnValue(cell.getStringCellValue())
//                        .build();
//                errorRows.add(excelRowException);
//            }
//
//        } else {
//            ExcelRowException excelRowException = ExcelRowException.builder()
//                    .rowIndex(index + 1)
//                    .columnIndex(cell.getColumnIndex())
//                    .columnName(ARTIST_NAME.getColumnName())
//                    .type(INVALID_CELL_VALUE_TYPE)
//                    .build();
//            errorRows.add(excelRowException);
//        }
    }

    private void validateTrackNameCell(Cell cell, Row row) {
        // 엑셀파일에서 트랙명이 null인 경우
        if (atoDistributorCellValidator.hasCellNullValue(cell)) {
            ExcelRowException excelRowException = atoDistributorCellValidator.generateException(TRACK_NAME, NULL_CELL, cell, row.getRowNum());
            errorRows.add(excelRowException);
        }
    }

    private boolean hasNotExistedArtist(Cell cell) {
        String artistName = cell.getStringCellValue();
        List<String> artistExtractedNames = NameExtractor.getExtractedNames(artistName);

        for (String artistExtractedName : artistExtractedNames) {
            Optional<Member> memberFindByEnName = memberRepository.findMemberByEnName(artistExtractedName);
            if (memberFindByEnName.isPresent()) {
                return false;
            }
            Optional<Member> memberFindByKoName = memberRepository.findMemberByName(artistExtractedName);
            if (memberFindByKoName.isPresent()) {
                return false;
            }
        }
        return true;
    }
}
