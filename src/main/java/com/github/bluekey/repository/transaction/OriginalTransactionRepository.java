package com.github.bluekey.repository.transaction;

import com.github.bluekey.entity.transaction.OriginalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OriginalTransactionRepository extends JpaRepository<OriginalTransaction, Long> {

    List<OriginalTransaction> findAllByUploadAtAndIsRemovedFalse(String uploadAt);
    List<OriginalTransaction> findAllByIsCompletedFalseAndIsRemovedFalse();
    Optional<OriginalTransaction> findOriginalTransactionByFileNameAndUploadAtAndIsRemovedFalse(String fileName, String uploadAt);

    default OriginalTransaction findByIdOrElseThrow(Long id) {
        return this.findById(id).orElseThrow(() ->
                new RuntimeException("존재하지 않는 pk 입니다.")
        );
    }
}
