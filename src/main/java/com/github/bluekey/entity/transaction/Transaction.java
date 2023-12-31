package com.github.bluekey.entity.transaction;

import com.github.bluekey.entity.BaseTimeEntity;
import com.github.bluekey.entity.track.Track;
import com.github.bluekey.entity.track.TrackMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "transaction", indexes = @Index(name = "transaction_duration", columnList = "duration"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "track_id")
	private Track track;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "original_transaction_id")
	private OriginalTransaction originalTransaction;

	private String duration;
	private Double amount;

	@Builder
	public Transaction(Track track, OriginalTransaction originalTransaction, String duration, Double amount) {
		this.track = track;
		this.originalTransaction = originalTransaction;
		this.duration = duration;
		this.amount = amount;
	}

	public void updateAmount(Double amount) {
		this.amount = this.amount + amount;
	}

	public String getYear() {
		return duration.split("-")[0];
	}

	public String getMonth() {
		return duration.split("-")[1];
	}
}
