package com.github.bluekey.entity.notification;

import com.github.bluekey.entity.BaseTimeEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name="request_authority")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestAuthority extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_authority_id")
	private Long id;

	@Column(name = "sender_id", nullable = false)
	private Long senderId;

	@Column(name = "confirm_at")
	private LocalDateTime confirmAt;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private RequestStatus status;

	@Builder
	public RequestAuthority(Long senderId) {
		this.senderId = senderId;
		this.status = RequestStatus.PENDING;
	}

	public void confirm(RequestStatus status) {
		this.confirmAt = LocalDateTime.now();
		this.status = status;
	}
}
