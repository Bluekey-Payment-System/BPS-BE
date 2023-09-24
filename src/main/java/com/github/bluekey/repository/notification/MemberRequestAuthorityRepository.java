package com.github.bluekey.repository.notification;

import com.github.bluekey.entity.notification.MemberRequestAuthority;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRequestAuthorityRepository extends
		JpaRepository<MemberRequestAuthority, Long> {
	Optional<MemberRequestAuthority> findMemberRequestAuthorityByMemberIdAndRequestAuthorityId(Long memberId, Long requestAuthorityId);

	default void findMemberRequestAuthorityByMemberIdAndRequestAuthorityIdOrElseThrow(Long memberId, Long requestAuthorityId) {
		this.findMemberRequestAuthorityByMemberIdAndRequestAuthorityId(memberId, requestAuthorityId)
				.orElseThrow(() ->
						new BusinessException(ErrorCode.AUTHORIZATION_FAILED)
				);
	}
}
