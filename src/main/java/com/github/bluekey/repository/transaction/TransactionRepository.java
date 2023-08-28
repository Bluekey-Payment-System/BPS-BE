package com.github.bluekey.repository.transaction;

import com.github.bluekey.entity.track.TrackMember;
import com.github.bluekey.entity.transaction.OriginalTransaction;
import com.github.bluekey.entity.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findTransactionByOriginalTransactionAndDurationAndTrackMember(
            OriginalTransaction originalTransaction,
            String duration,
            TrackMember trackMember
    );

    List<Transaction> findTransactionsByDuration(String duration);

    List<Transaction> findTransactionsByDurationBetween(String startDate, String endDate);
}
