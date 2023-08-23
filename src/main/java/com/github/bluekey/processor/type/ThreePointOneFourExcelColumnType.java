package com.github.bluekey.processor.type;

import lombok.Getter;

@Getter
public enum ThreePointOneFourExcelColumnType {
    MEDIA("MEDIA", 0),
    SERVICE_COMPANY("서비스사", 1),
    ALBUM_NAME("앨범명", 2),
    ALBUM_CODE("앨범코드", 3),
    TRACK_NAME("트랙명", 4),
    TRACK_CODE("트랙코드", 5),
    ARTIST_NAME("아티스트명", 6),
    OWN_COMPANY("권리사", 7),
    STREAMING("스트리밍", 8),
    DOWNLOADS("다운로드", 9),
    ETC_CNT("기타수량", 10),
    NEIGHBORING_RIGHTS_FEE("저작인접권료", 11),
    DISTRIBUTION_FEE("유통수수료", 12),
    AMOUNT("인세", 13);

    private final String columnName;
    private final int index;

    ThreePointOneFourExcelColumnType(String columnName, int index) {
        this.columnName = columnName;
        this.index = index;
    }
}
