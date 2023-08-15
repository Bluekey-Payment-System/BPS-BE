package com.github.bluekey.entity.member;

import com.github.bluekey.entity.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "login_id", nullable = false)
	private String loginId;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "en_name")
	private String enName;

	// commission rate은 int가 맞는지?
	@Column(name = "commission_rate")
	private Integer commissionRate;

	@Column(name = "type", nullable = false)
	private MemberType type;

	@Column(name = "role", nullable = false)
	private MemberRole role;

	@Column(name = "profile_image")
	private String profileImage;

	@Builder
	public Member(String email, String loginId, String password, String name, String enName, Integer commissionRate,
			MemberType type, MemberRole role, String profileImage) {
		this.email = email;
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.enName = enName;
		this.commissionRate = commissionRate;
		this.type = type;
		this.role = role;
		this.profileImage = profileImage;
	}
}
