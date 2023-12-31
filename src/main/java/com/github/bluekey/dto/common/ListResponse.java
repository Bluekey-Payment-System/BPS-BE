package com.github.bluekey.dto.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListResponse<T> {
    private int totalItems;
    private List<T> contents = new ArrayList<>();

    public ListResponse(int totalItems, List<T> contents) {
        this.totalItems = totalItems;
        this.contents = contents;
    }
}