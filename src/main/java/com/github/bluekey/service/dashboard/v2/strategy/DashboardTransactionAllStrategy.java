package com.github.bluekey.service.dashboard.v2.strategy;

public interface DashboardTransactionAllStrategy<T> extends DashboardTransactionGroupingStrategy, DashboardTransactionSortingStrategy<T>, DashboardTransactionFilteringStrategy<T> {
}
