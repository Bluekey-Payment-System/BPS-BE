package com.github.bluekey.repository.notification;

import com.github.bluekey.entity.notification.MemberRequestAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRequestAuthorityRepository extends
		JpaRepository<MemberRequestAuthority, Long> {

}
