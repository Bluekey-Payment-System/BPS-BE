package com.github.bluekey.processor;

import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.entity.transaction.ExcelDistributorType;
import com.github.bluekey.entity.transaction.OriginalTransaction;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.processor.type.AtoExcelColumnType;
import com.github.bluekey.processor.type.ThreePointOneFourExcelColumnType;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
import com.github.bluekey.repository.track.TrackRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelFileDBMigrationProcessManager implements ProcessManager {
    private static final int ATO_ACTIVE_EXCEL_SHEET_INDEX = 1;
    private static final int THREE_POINT_ONE_FOUR_ACTIVE_EXCEL_SHEET_INDEX = 2;

    private static final int ATO_DATA_ROW_START_INDEX = 5;
    private static final int THREE_POINT_ONE_FOUR_DATA_ROW_START_INDEX = 4;

    private final MemberRepository memberRepository;
    private final TrackMemberRepository trackMemberRepository;
    private final TrackRepository trackRepository;
    private final TransactionRepository transactionRepository;

    private Map<Workbook, OriginalTransaction> workbooks = new HashMap<>();

    @Override
    public void process() {
        for (Map.Entry<Workbook, OriginalTransaction> workbook : workbooks.entrySet()) {
            Workbook distributorWorkbook = workbook.getKey();
            ExcelDistributorType distributorType = workbook.getValue().getDistributorType();
            if (distributorType.equals(ExcelDistributorType.ATO)) {
                Sheet sheet = distributorWorkbook.getSheetAt(ATO_ACTIVE_EXCEL_SHEET_INDEX);
                atoWorkbookProcess(sheet, workbook.getValue());
            }
            if (distributorType.equals(ExcelDistributorType.THREE_POINT_ONE_FOUR)) {
                Sheet sheet = distributorWorkbook.getSheetAt(THREE_POINT_ONE_FOUR_ACTIVE_EXCEL_SHEET_INDEX);
                threePointOneFourWorkbookProcess(sheet, workbook.getValue());
            }
        }

    }

    public void updateWorkbooks(Map<Workbook, OriginalTransaction> workbooks) {
        this.workbooks = workbooks;
    }

    private void threePointOneFourWorkbookProcess(Sheet sheet, OriginalTransaction originalTransaction) {
        for (int i = THREE_POINT_ONE_FOUR_DATA_ROW_START_INDEX; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            migrateThreePointOneFourExcelDataToDB(row, originalTransaction);
        }
    }

    private void migrateThreePointOneFourExcelDataToDB(Row row, OriginalTransaction originalTransaction) {
        DataFormatter dataFormatter = new DataFormatter();

        Cell artistCell = row.getCell(ThreePointOneFourExcelColumnType.ARTIST_NAME.getIndex());
        Cell trackCell = row.getCell(ThreePointOneFourExcelColumnType.TRACK_NAME.getIndex());
        Cell amountCell = row.getCell(ThreePointOneFourExcelColumnType.AMOUNT.getIndex());

        String artistName = dataFormatter.formatCellValue(artistCell);
        String trackName = dataFormatter.formatCellValue(trackCell);
        Double amount = amountCell.getNumericCellValue();

        List<String> artistExtractedNames = NameExtractor.getExtractedNames(artistName);

        migrate(artistExtractedNames, trackName, amount, originalTransaction);
    }

    private void atoWorkbookProcess(Sheet sheet, OriginalTransaction originalTransaction) {
        for (int i = ATO_DATA_ROW_START_INDEX; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            migrateAtoExcelDataToDB(row, originalTransaction);
        }
    }

    private void migrateAtoExcelDataToDB(Row row, OriginalTransaction originalTransaction) {
        DataFormatter dataFormatter = new DataFormatter();

        Cell artistCell = row.getCell(AtoExcelColumnType.ARTIST_NAME.getIndex());
        Cell trackCell = row.getCell(AtoExcelColumnType.TRACK_NAME.getIndex());
        Cell amountCell = row.getCell(AtoExcelColumnType.AMOUNT.getIndex());

        String artistName = dataFormatter.formatCellValue(artistCell);
        String trackName = dataFormatter.formatCellValue(trackCell);
        Double amount = amountCell.getNumericCellValue();

        List<String> artistExtractedNames = NameExtractor.getExtractedNames(artistName);

        migrate(artistExtractedNames, trackName, amount, originalTransaction);
    }

    private void migrate(List<String> artistExtractedNames, String trackName, Double amount, OriginalTransaction originalTransaction) {
        for (String artistExtractedName : artistExtractedNames) {
            Optional<Track> trackFindByEnName = trackRepository.findTrackByEnNameIgnoreCase(trackName);
            if (trackFindByEnName.isPresent()) {
                Optional<TrackMember> trackMemberFindByName = trackMemberRepository.findTrackMemberByNameAndTrack(artistExtractedName, trackFindByEnName.get());
                if (trackMemberFindByName.isPresent()) {
                    TrackMember trackMemberByEnName = trackMemberFindByName.get();
                    Optional<Transaction> transaction = transactionRepository.findTransactionByOriginalTransactionAndDurationAndTrackMember(
                            originalTransaction,
                            originalTransaction.getUploadAt(),
                            trackMemberByEnName
                    );
                    if (transaction.isPresent()) {
                        Transaction existedTransaction = transaction.get();
                        existedTransaction.updateAmount(amount);
                        transactionRepository.save(existedTransaction);
                    } else {
                        transactionRepository.save(createNewTransaction(amount, originalTransaction, trackMemberByEnName));
                    }
                }
            }
            Optional<Track> trackFindByName = trackRepository.findTrackByNameIgnoreCase(trackName);

            if (trackFindByName.isPresent() && !trackFindByName.get().getEnName().equals(trackFindByName.get().getName())) {
                Optional<TrackMember> trackMemberFindByName = trackMemberRepository.findTrackMemberByNameAndTrack(artistExtractedName, trackFindByName.get());
                if (trackMemberFindByName.isPresent()) {
                    TrackMember trackMemberByName = trackMemberFindByName.get();
                    Optional<Transaction> transaction = transactionRepository.findTransactionByOriginalTransactionAndDurationAndTrackMember(
                            originalTransaction,
                            originalTransaction.getUploadAt(),
                            trackMemberByName
                    );
                    if (transaction.isPresent()) {
                        Transaction existedTransaction = transaction.get();
                        existedTransaction.updateAmount(amount);
                        transactionRepository.save(existedTransaction);
                    } else {
                        transactionRepository.save(createNewTransaction(amount, originalTransaction, trackMemberByName));
                    }
                }
            }
        }
    }

    private Transaction createNewTransaction(Double amount, OriginalTransaction originalTransaction, TrackMember trackMember) {
        return Transaction.builder()
                .amount(amount)
                .duration(originalTransaction.getUploadAt())
                .trackMember(trackMember)
                .originalTransaction(originalTransaction)
                .build();
    }
}
