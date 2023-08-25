package com.github.bluekey.processor.type;

import lombok.Getter;

@Getter
public enum MafiaExcelColumnType implements ColumnType{
    ALBUM_NAME("앨범명", 1),
    TRACK_NAME("곡명", 2);

    private final String columnName;
    private final int index;

    MafiaExcelColumnType(String columnName, int index) {
        this.columnName = columnName;
        this.index = index;
    }

    public static MafiaExcelColumnType valueOfIndex(int index) {
        return values()[index];
    }

}
