package com.github.bluekey.service.dashboard.v2.strategy;

import java.util.stream.Stream;

public interface DashboardTransactionSortingStrategy<T> {
    Stream<T> sort();
    Stream<T> sortByReverse();
}
