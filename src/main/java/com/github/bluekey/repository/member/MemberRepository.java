package com.github.bluekey.repository.member;

import com.github.bluekey.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByEmail(String email);
	boolean existsByLoginId(String loginId);
}
