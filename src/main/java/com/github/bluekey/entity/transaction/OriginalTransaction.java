package com.github.bluekey.entity.transaction;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OriginalTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "original_transaction_id")
	private Long id;

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
			throw new IllegalStateException("이미 배치 작업이 완료된 transaction 입니다.");
		}
		this.isCompleted = true;
	}
}
