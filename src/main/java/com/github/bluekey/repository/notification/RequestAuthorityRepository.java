package com.github.bluekey.repository.notification;

import com.github.bluekey.entity.notification.RequestAuthority;
import com.github.bluekey.entity.notification.RequestStatus;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestAuthorityRepository extends JpaRepository<RequestAuthority, Long> {

	Optional<RequestAuthority> findRequestAuthorityByIdAndStatus(Long id, RequestStatus status);

	default RequestAuthority findRequestAuthorityByIdAndStatusOrElseThrow(Long id, RequestStatus status) {
		return this.findRequestAuthorityByIdAndStatus(id, status).orElseThrow(() ->
				new BusinessException(ErrorCode.REQUEST_AUTHORITY_NOT_PENDING)
		);
	}
}
