package com.github.bluekey.processor.type;

import lombok.Getter;

@Getter
public enum AtoExcelColumnType {
    CP("CP", 0),
    SERVICE_AT("서비스월", 1),
    ALBUM_NAME("앨범명", 2),
    ARTIST_NAME("가수명", 3),
    TRACK_NAME("곡제목", 4),
    AGENCY_CODE("기획사", 5),
    SERVICE_NAME("서비스명", 6),
    SERVICE_SUB_NAME("서비스명2", 7),
    SUPPLY_VALUE("공급가액", 8),
    CHARGE("수수료", 9),
    AMOUNT("정산금액", 10);

    private final String columnName;
    private final int index;

    AtoExcelColumnType(String columnName, int index) {
        this.columnName = columnName;
        this.index = index;
    }
}
