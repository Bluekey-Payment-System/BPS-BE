package com.github.bluekey.entity.member;

import com.github.bluekey.entity.BaseTimeEntity;
import com.github.bluekey.entity.notification.MemberRequestAuthority;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Embedded
	private Email email;

	@Column(name = "login_id", nullable = false)
	private String loginId;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "en_name")
	private String enName;

	@Column(name = "commission_rate")
	private Integer commissionRate;

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberType type;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberRole role;

	@Column(name = "profile_image")
	private String profileImage;

	@ColumnDefault("0")
	private int rejectCount;

	@OneToMany(
			mappedBy = "member",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<MemberRequestAuthority> memberRequestAuthorities = new ArrayList<>();

	@Builder(builderClassName = "ByArtistBuilder", builderMethodName = "ByArtistBuilder")
	public Member(String email, String loginId, String password, String name, String enName, Integer commissionRate, String profileImage) {
		this.email = new Email(email);
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.enName = enName;
		this.commissionRate = commissionRate;
		this.type = MemberType.USER;
		this.role = MemberRole.ARTIST;
		this.profileImage = profileImage;
		this.rejectCount = 0;
	}

	@Builder(builderClassName = "ByAdminBuilder", builderMethodName = "ByAdminBuilder")
	public Member(String email, String loginId, String password, String name, MemberRole role) {
		this.email = new Email(email);;
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.commissionRate = 0;
		this.type = MemberType.ADMIN;
		this.role = role;
	}

	public void updateProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateEmail(String email) {
		this.email = new Email(email);
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateEnName(String enName) {
		this.enName = enName;
	}

	public void updateCommissionRate(Integer commissionRate) {
		validateCommissionRate(commissionRate);
		this.commissionRate = commissionRate;
	}

	public void updateRole(MemberRole role) {
		this.role = role;
	}

	public void memberRemoved() {
		if (isRemoved()) {
			throw new BusinessException(ErrorCode.MEMBER_ALREADY_REMOVED);
		}
		this.remove();
	}

	public int getRejectCount() {
		return rejectCount;
	}

	public void reject() {
		this.rejectCount += 1;
	}

	public boolean isUser() {
		return this.type == MemberType.USER;
	}

	public boolean isAdmin() {
		return this.type == MemberType.ADMIN;
	}

	private void validateCommissionRate(Integer commissionRate) {
		if (commissionRate < 0) {
			throw new IllegalArgumentException("Percentage value must not be negative.");
		}

		if (commissionRate > 100) {
			throw new IllegalArgumentException("Percentage value cannot exceed 100.");
		}
	}
}
