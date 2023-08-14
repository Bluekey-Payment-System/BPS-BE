package com.github.bluekey.processor;

import lombok.Getter;

@Getter
public enum MusicDistributorType {
    ATO("L2110A11_\\d{6}$"),
    THREE_POINT_ONE_FOUR("^\\d{6}-[a-zA-Z0-9]+$"),
    MAFIA("마피아뮤직_\\d{6}\\$");

    private final String pattern;

    MusicDistributorType(String pattern) {
        this.pattern = pattern;
    }
}
