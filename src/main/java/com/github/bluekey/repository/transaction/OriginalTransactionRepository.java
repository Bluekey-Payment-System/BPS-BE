package com.github.bluekey.repository.transaction;

import com.github.bluekey.entity.transaction.OriginalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OriginalTransactionRepository extends JpaRepository<OriginalTransaction, Long> {

    List<OriginalTransaction> findAllByUploadAt(String uploadAt);
}
