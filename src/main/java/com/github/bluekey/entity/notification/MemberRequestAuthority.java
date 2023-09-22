package com.github.bluekey.entity.notification;

import com.github.bluekey.entity.member.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name="member_request_authority")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequestAuthority {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_request_authority_id")
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private Member member;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "request_authority_id")
	private RequestAuthority requestAuthority;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Builder
	public MemberRequestAuthority(Member member, RequestAuthority requestAuthority) {
		this.member = member;
		this.requestAuthority = requestAuthority;
		this.createdAt = LocalDateTime.now();
	}
}
