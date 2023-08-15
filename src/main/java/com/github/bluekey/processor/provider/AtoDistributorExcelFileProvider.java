package com.github.bluekey.processor.provider;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.processor.ExcelRowException;
import com.github.bluekey.processor.NameExtractor;
import com.github.bluekey.processor.type.AtoExcelColumnType;
import com.github.bluekey.processor.type.ExcelRowExceptionType;
import com.github.bluekey.repository.member.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class AtoDistributorExcelFileProvider implements ExcelFileProvider {
    private static final int ACTIVE_EXCEL_SHEET_INDEX = 1;
    private static final String ACTIVE_EXCEL_SHEET_NAME = "전체매출내역";
    private static final int MIN_COLUMN_INDEX = 0;
    private static final int MAX_COLUMN_INDEX = 10;
    private static final int HEADER_ROW_INDEX = 3;
    private static final int DATA_ROW_START_INDEX = 5;
    private final List<ExcelRowException> errorRows = new ArrayList<>();

    private final Workbook workbook;
    private final MemberRepository memberRepository;

    public AtoDistributorExcelFileProvider(MultipartFile file, MemberRepository memberRepository) {
        this.workbook = setWorkBook(file);
        this.memberRepository = memberRepository;
    }

    @Override
    public Sheet getActiveSheet() {
        if (hasValidSheetName()) {
            return workbook.getSheetAt(ACTIVE_EXCEL_SHEET_INDEX);
        }
        throw new RuntimeException("Invalid sheet name");
    }

    @Override
    public void process(Sheet sheet) {
        for (int i = DATA_ROW_START_INDEX; i<= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            processCell(row);
        }
        System.out.println(errorRows);
    }

    @Override
    public boolean hasValidSheetName() {
        Sheet sheet = workbook.getSheetAt(ACTIVE_EXCEL_SHEET_INDEX);
        return sheet.getSheetName().equals(ACTIVE_EXCEL_SHEET_NAME);
    }

    @Override
    public List<ExcelRowException> getErrors() {
        return errorRows;
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
            validateCell(cell, row.getRowNum(), row);
        }
    }

    private void validateCell(Cell cell, int index, Row row) {
        if (AtoExcelColumnType.ARTIST_NAME.getIndex() == cell.getColumnIndex()) {
            validateArtistNameCell(cell, index);
        }
        if (AtoExcelColumnType.ALBUM_NAME.getIndex() == cell.getColumnIndex()) {
            validateAlbumNameCell(cell, index, row);
        }
    }

    private void validateAlbumNameCell(Cell cell, int index, Row row) {
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            Cell cellArtist = row.getCell(AtoExcelColumnType.ARTIST_NAME.getIndex());
            Cell cellTrack = row.getCell(AtoExcelColumnType.TRACK_NAME.getIndex());
            if (cell.getNumericCellValue() == 0 && cellArtist != null && cellTrack != null) {
                ExcelRowException excelRowException = ExcelRowException.builder()
                        .rowIndex(index +1)
                        .columnIndex(cell.getColumnIndex())
                        .columnName(AtoExcelColumnType.ALBUM_NAME.getColumnName())
                        .type(ExcelRowExceptionType.ALLOW_EXCEPTION_CASE)
                        .build();
                errorRows.add(excelRowException);
            }
        }
    }

    private void validateArtistNameCell(Cell cell, int index) {
        if (cell.getCellType().equals(CellType.STRING)) {
            if(cell.getStringCellValue() == null) {
                ExcelRowException excelRowException = ExcelRowException.builder()
                        .rowIndex(index + 1)
                        .columnIndex(cell.getColumnIndex())
                        .columnName(AtoExcelColumnType.ARTIST_NAME.getColumnName())
                        .type(ExcelRowExceptionType.NULL_CELL)
                        .build();
                errorRows.add(excelRowException);
            }
            if (hasNotExistedArtist(cell)) {
                ExcelRowException excelRowException = ExcelRowException.builder()
                        .rowIndex(index + 1)
                        .columnIndex(cell.getColumnIndex())
                        .columnName(AtoExcelColumnType.ARTIST_NAME.getColumnName())
                        .type(ExcelRowExceptionType.NOT_EXIST)
                        .columnValue(cell.getStringCellValue())
                        .domain("ARTIST")
                        .build();
                errorRows.add(excelRowException);
            }

        } else {
            ExcelRowException excelRowException = ExcelRowException.builder()
                    .rowIndex(index + 1)
                    .columnIndex(cell.getColumnIndex())
                    .columnName(AtoExcelColumnType.ARTIST_NAME.getColumnName())
                    .type(ExcelRowExceptionType.INVALID_CELL_VALUE_TYPE)
                    .build();
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
