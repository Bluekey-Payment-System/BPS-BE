package com.github.bluekey.service.dashboard.v2;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.transaction.Transaction;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.repository.album.AlbumRepository;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BarChartDashboardServiceV2 {
    private final MemberRepository memberRepository;
    private final AlbumRepository albumRepository;
    private final TransactionRepository transactionRepository;
    private final DashboardTransactionFactory dashboardTransactionFactory;

    @Transactional(readOnly = true)
    public void getAlbumMonthlyTrends(String startDate, String endDate, Long albumId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        albumRepository.findById(albumId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ALBUM_NOT_FOUND));

        List<Transaction> transactions = transactionRepository.findTransactionsByDurationBetween(startDate, endDate);
        dashboardTransactionFactory.setDashboardTransactionStrategy(member, transactions);
        Map<String, Double> amountsGroupedByMonth = dashboardTransactionFactory.getAmountGroupedByMonth();
    }
}
