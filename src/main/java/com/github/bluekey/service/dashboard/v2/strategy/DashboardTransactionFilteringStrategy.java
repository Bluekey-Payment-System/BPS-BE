package com.github.bluekey.service.dashboard.v2.strategy;

import java.util.stream.Stream;

public interface DashboardTransactionFilteringStrategy<T> {
    Stream<T> filteredByAlbumId(Long albumId);
}
