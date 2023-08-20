package com.github.bluekey.s3.manager;

import lombok.Getter;

@Getter
public enum S3PrefixType {

    IMAGE("image/"),
    EXCEL("excel/");

    private final String value;
    S3PrefixType(String value) {
        this.value = value;
    }
}
