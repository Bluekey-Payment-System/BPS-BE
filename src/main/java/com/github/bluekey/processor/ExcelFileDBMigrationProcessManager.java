package com.github.bluekey.processor;

import com.github.bluekey.entity.album.Album;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.entity.transaction.ExcelDistributorType;
import com.github.bluekey.entity.transaction.OriginalTransaction;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.processor.type.AtoExcelColumnType;
import com.github.bluekey.processor.type.MafiaExcelColumnType;
import com.github.bluekey.processor.type.ThreePointOneFourExcelColumnType;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.track.TrackMemberRepository;
import com.github.bluekey.repository.track.TrackRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelFileDBMigrationProcessManager implements ProcessManager {
    private static final int ATO_ACTIVE_EXCEL_SHEET_INDEX = 1;
    private static final int THREE_POINT_ONE_FOUR_ACTIVE_EXCEL_SHEET_INDEX = 2;
    private static final int MAFIA_ACTIVE_EXCEL_SHEET_INDEX = 0;

    private static final int ATO_DATA_ROW_START_INDEX = 5;
    private static final int THREE_POINT_ONE_FOUR_DATA_ROW_START_INDEX = 4;
    private static final int MAFIA_DATA_ROW_START_INDEX = 3;
    private static final int MAFIA_DATA_HEADER_INDEX = 2;
    private static final int MAFIA_DATA_COLUMN_INDEX = 3;
    private static final String MAFIA_DATA_OVERSEAS = "해외";
    private static final String MAFIA_DATA_DOMESTIC = "국내";

    private final MemberRepository memberRepository;
    private final TrackMemberRepository trackMemberRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final TransactionRepository transactionRepository;
    private String albumNameInMafia;
    private String trackNameInMafia;

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
            if (distributorType.equals(ExcelDistributorType.MAFIA)) {
                Sheet sheet = distributorWorkbook.getSheetAt(MAFIA_ACTIVE_EXCEL_SHEET_INDEX);
                mafiaWorkbookProcess(sheet, workbook.getValue());

            }
        }

    }

    public void updateWorkbooks(Map<Workbook, OriginalTransaction> workbooks) {
        this.workbooks = workbooks;
    }

    private void mafiaWorkbookProcess(Sheet sheet, OriginalTransaction originalTransaction) {
        String file = originalTransaction.getFileName();
        String fileName = file.split("\\.")[0];
        String transactionAt = fileName.split("_")[1];
        List<String> extractedArtistNames = new ArrayList<>();
        String artistName = fileName.split("_")[2];
        extractedArtistNames.add(artistName);
        Row header = sheet.getRow(MAFIA_DATA_HEADER_INDEX);
        int columnIndex = getTransactionAt(header, transactionAt);

        for (int i = MAFIA_DATA_ROW_START_INDEX; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            migrateMafiaExcelDataToDB(row, originalTransaction, columnIndex, extractedArtistNames);
        }
    }

    private void migrateMafiaExcelDataToDB(Row row, OriginalTransaction originalTransaction, int transactionAt, List<String> artistNames) {

        Cell albumCell = row.getCell(MafiaExcelColumnType.ALBUM_NAME.getIndex());
        if (!albumCell.getStringCellValue().isBlank()) {
            this.albumNameInMafia = albumCell.getStringCellValue();
        }
        Cell trackCell = row.getCell(MafiaExcelColumnType.TRACK_NAME.getIndex());
        if (!trackCell.getStringCellValue().isBlank()) {
            this.trackNameInMafia = trackCell.getStringCellValue();
        }

        Cell amountIndexCell = row.getCell(MAFIA_DATA_COLUMN_INDEX);
        Cell amountCell = row.getCell(transactionAt);

        if (amountIndexCell.getStringCellValue().equals(MAFIA_DATA_DOMESTIC) || amountIndexCell.getStringCellValue().equals(MAFIA_DATA_OVERSEAS)) {
            if (trackCell.getStringCellValue().isBlank()) {
//                log.info("albumCell = {}", albumCell.getStringCellValue());
//                log.info("trackCell = {}", trackCell.getStringCellValue());
//                log.info("amountCell = {}", amountCell.getNumericCellValue());
                migrate(artistNames, albumNameInMafia, trackNameInMafia, amountCell.getNumericCellValue(), originalTransaction);
            }
        }
    }

    private void migrate(List<String> artistExtractedNames, String albumName, String trackName, Double amount, OriginalTransaction originalTransaction) {
        Album album = null;
        List<Album> albums = albumRepository.findAlbumByNameIgnoreCaseOrEnNameIgnoreCaseAndIsRemoved(albumName, albumName, false);

        boolean isExistTrackByName = false;
        // 앨범이 존재할 경우
        if (albums.size() >= 1) {
            if (albums.size() >= 2) {
                Album album1 = albums.get(0);
                Album album2 = albums.get(1);
                if (album1.getId().equals(album2.getId()) || album1.getName().equals(album2.getName()) || album1.getEnName().equals((album2.getEnName()))) {
                    album = album1;
                }
            } else if(albums.size() == 1) {
                album = albums.get(0);
            }
            
            Album findAlbum = album;
//            Optional<Track> trackByName = trackRepository.findTrackByNameIgnoreCaseAndAlbum(trackName, findAlbum);
            List<Track> tracksByName = trackRepository.findAllByNameIgnoreCaseAndAlbum(trackName, findAlbum);
            Track track = null;
            if (tracksByName.size() == 1) {
                track = tracksByName.get(0);
            }

            if (tracksByName.size() > 1) {
                for (Track findTrack : tracksByName) {
                    if (!findTrack.isRemoved()) {
                        for (TrackMember trackMember : findTrack.getTrackMembers()) {
                            for (String artistName : artistExtractedNames) {
                                if (trackMember.getName().equals(artistName)) {
                                    track = findTrack;
                                }
                            }
                        }
                    }
                }
            }

            if (track != null) {
                Track findTrack = track;

                Optional<Transaction> transaction = transactionRepository.findTransactionsByOriginalTransactionAndDurationAndTrack(
                        originalTransaction,
                        originalTransaction.getUploadAt(),
                        findTrack
                );

                if (transaction.isPresent()) {
                    Transaction existedTransaction = transaction.get();
                    existedTransaction.updateAmount(amount);
                    transactionRepository.save(existedTransaction);
                } else {
                    transactionRepository.save(createNewTransaction(amount, originalTransaction, findTrack));
                }
                isExistTrackByName = true;
            }

//            Optional<Track> trackByEnName = trackRepository.findTrackByEnNameIgnoreCaseAndAlbum(trackName, findAlbum);
            List<Track> tracksByEnName = trackRepository.findAllByEnNameIgnoreCaseAndAlbum(trackName, findAlbum);
            Track enTrack = null;
            if (tracksByEnName.size() == 1) {
                enTrack = tracksByEnName.get(0);
            }
            if (tracksByEnName.size() > 1) {
                for (Track findTrack : tracksByEnName) {
                    if (!findTrack.isRemoved()) {
                        for (TrackMember trackMember : findTrack.getTrackMembers()) {
                            for (String artistName : artistExtractedNames) {
                                if (trackMember.getName().equals(artistName)) {
                                    enTrack = findTrack;
                                }
                            }
                        }
                    }
                }
            }

            if (enTrack != null && !isExistTrackByName) {
                Track findTrack = enTrack;

                Optional<Transaction> transaction = transactionRepository.findTransactionsByOriginalTransactionAndDurationAndTrack(
                        originalTransaction,
                        originalTransaction.getUploadAt(),
                        findTrack
                );

                if (transaction.isPresent()) {
                    Transaction existedTransaction = transaction.get();
                    existedTransaction.updateAmount(amount);
                    transactionRepository.save(existedTransaction);
                } else {
                    transactionRepository.save(createNewTransaction(amount, originalTransaction, findTrack));
                }
            }

        }

        // 앨범이 존재하지 않는 경우 경우 -> 유튜브인 경우 album이 0으로 들어올 경우
        if (albums.size() == 0) {
            List<Track> tracks = trackRepository.findTrackByNameIgnoreCaseOrEnNameIgnoreCase(trackName, trackName);
            if (tracks.size() == 1) {
                for (Track track : tracks) {
                    for (TrackMember trackMember : track.getTrackMembers()) {
                        if (artistExtractedNames.contains(trackMember.getName())) {
                            Optional<Transaction> transaction = transactionRepository.findTransactionsByOriginalTransactionAndDurationAndTrack(
                                    originalTransaction,
                                    originalTransaction.getUploadAt(),
                                    track
                            );

                            if (transaction.isPresent()) {
                                Transaction existedTransaction = transaction.get();
                                existedTransaction.updateAmount(amount);
                                transactionRepository.save(existedTransaction);
                            } else {
                                transactionRepository.save(createNewTransaction(amount, originalTransaction, track));
                            }
                        }
                    }
                }
            } else if(tracks.size() > 1) {
                Comparator<Track> trackNameComparator = Comparator.comparing(track -> track.getAlbum().getName());

                Track firstTrack = tracks.stream()
                        .min(trackNameComparator)
                        .orElse(null);

                for (TrackMember trackMember : firstTrack.getTrackMembers()) {
                    if (artistExtractedNames.contains(trackMember.getName())) {
                        Optional<Transaction> transaction = transactionRepository.findTransactionsByOriginalTransactionAndDurationAndTrack(
                                originalTransaction,
                                originalTransaction.getUploadAt(),
                                firstTrack
                        );

                        if (transaction.isPresent()) {
                            Transaction existedTransaction = transaction.get();
                            existedTransaction.updateAmount(amount);
                            transactionRepository.save(existedTransaction);
                        } else {
                            transactionRepository.save(createNewTransaction(amount, originalTransaction, firstTrack));
                        }
                    }
                }
            }
        }
    }
    private int getTransactionAt(Row row, String transactionAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        for (Cell cell: row) {
            if (cell.getColumnIndex() >=4 && cell.getColumnIndex() <=16) {
                LocalDate date1 = cell.getLocalDateTimeCellValue().toLocalDate();
                LocalDate date2 = LocalDate.parse(transactionAt + "01", formatter); // Assuming input2 is in yyyyMM format
                if (date1.equals(date2)) {
                    return cell.getColumnIndex();
                }
            }

        }
        throw new RuntimeException("날짜 데이터 파싱 에러");
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
        Cell albumCell = row.getCell(ThreePointOneFourExcelColumnType.ALBUM_NAME.getIndex());
        Cell trackCell = row.getCell(ThreePointOneFourExcelColumnType.TRACK_NAME.getIndex());
        Cell amountCell = row.getCell(ThreePointOneFourExcelColumnType.AMOUNT.getIndex());

        String artistName = dataFormatter.formatCellValue(artistCell);
        String trackName = dataFormatter.formatCellValue(trackCell);
        String albumName = dataFormatter.formatCellValue(albumCell);
        Double amount = amountCell.getNumericCellValue();

        List<String> artistExtractedNames = NameExtractor.getExtractedNames(artistName);

        migrate(artistExtractedNames, albumName, trackName, amount, originalTransaction);
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
        Cell albumCell = row.getCell(AtoExcelColumnType.ALBUM_NAME.getIndex());
        Cell trackCell = row.getCell(AtoExcelColumnType.TRACK_NAME.getIndex());
        Cell amountCell = row.getCell(AtoExcelColumnType.AMOUNT.getIndex());

        String artistName = dataFormatter.formatCellValue(artistCell);
        String albumName = dataFormatter.formatCellValue(albumCell);
        String trackName = dataFormatter.formatCellValue(trackCell);
        Double amount = amountCell.getNumericCellValue();

        List<String> artistExtractedNames = NameExtractor.getExtractedNames(artistName);

        migrate(artistExtractedNames, albumName, trackName, amount, originalTransaction);
    }

    private Transaction createNewTransaction(Double amount, OriginalTransaction originalTransaction, Track track) {
        return Transaction.builder()
                .amount(amount)
                .duration(originalTransaction.getUploadAt())
                .track(track)
                .originalTransaction(originalTransaction)
                .build();
    }
}
