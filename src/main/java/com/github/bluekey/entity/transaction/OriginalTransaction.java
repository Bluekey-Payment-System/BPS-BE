package com.github.bluekey.entity.transaction;

import com.github.bluekey.exception.transaction.TransactionAlreadyUploadException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="original_transactions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OriginalTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "original_transaction_id")
	private Long id;

	@OneToMany(mappedBy = "originalTransaction", cascade = CascadeType.ALL)
	private List<Transaction> transactions = new ArrayList<>();

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private Boolean isCompleted;

	@Column(nullable = false)
	private String fileUrl;

	@Column(nullable = false)
	private String uploadAt;

	@Builder
	public OriginalTransaction(String fileName, String fileUrl, String uploadAt) {
		this.fileName = fileName;
		this.isCompleted = false;
		this.fileUrl = fileUrl;
		this.uploadAt = uploadAt;
	}

	public void updateBatchScheduleCompleted() {
		if (this.isCompleted) {
			throw new TransactionAlreadyUploadException(this.fileName);
		}
		this.isCompleted = true;
	}
}
