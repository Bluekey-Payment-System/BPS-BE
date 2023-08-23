package com.github.bluekey.repository.transaction;

import com.github.bluekey.entity.transaction.OriginalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OriginalTransactionRepository extends JpaRepository<OriginalTransaction, Long> {

    List<OriginalTransaction> findAllByUploadAt(String uploadAt);
    List<OriginalTransaction> findAllByIsCompletedFalseAndIsRemovedFalse();

    default OriginalTransaction findByIdOrElseThrow(Long id) {
        return this.findById(id).orElseThrow(() ->
                new RuntimeException("존재하지 않는 pk 입니다.")
        );
    }
}
