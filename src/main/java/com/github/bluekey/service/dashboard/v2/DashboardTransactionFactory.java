package com.github.bluekey.service.dashboard.v2;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.service.dashboard.v2.strategy.DashboardTransactionAllStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DashboardTransactionFactory {
    private DashboardTransactionAllStrategy<Transaction> dashboardTransactionStrategy;

    public void setDashboardTransactionStrategy(Member member, List<Transaction> transactions) {
        if(member.isAdmin()) {
            this.dashboardTransactionStrategy = new DashboardTransactionAdminClient(transactions);
        } else if(member.isUser()) {
            this.dashboardTransactionStrategy = new DashboardTransactionArtistClient(transactions, member.getId());
        }
    }

    /**
     * 예시 method 입니다!
     * @return
     */
    public Map<String, Double> getAmountGroupedByMonth() {
        return dashboardTransactionStrategy.groupedByMonth();
    }
}
