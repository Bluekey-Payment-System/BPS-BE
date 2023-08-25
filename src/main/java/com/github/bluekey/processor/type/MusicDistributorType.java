package com.github.bluekey.processor.type;

import com.github.bluekey.processor.provider.AtoDistributorExcelFileProvider;
import com.github.bluekey.processor.provider.MafiaDistributorExcelFileProvider;
import com.github.bluekey.processor.provider.ThreePointOneFourDistributorExcelFileProvider;
import lombok.Getter;

@Getter
public enum MusicDistributorType {
    ATO("L2110A11_\\d{6}$", AtoDistributorExcelFileProvider.class),
    THREE_POINT_ONE_FOUR("^^\\d{6}-\\p{IsHangul}+$", ThreePointOneFourDistributorExcelFileProvider.class),
    MAFIA("마피아_\\d{6}_\\p{IsHangul}+$", MafiaDistributorExcelFileProvider.class);

    private final String pattern;
    private final Class<?> cls;

    MusicDistributorType(String pattern, Class<?> cls ) {
        this.pattern = pattern;
        this.cls = cls;
    }
}
