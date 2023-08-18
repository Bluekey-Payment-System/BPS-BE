package com.github.bluekey.repository.member;

import com.github.bluekey.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByEmail(String email);
	boolean existsByLoginId(String loginId);

	Optional<Member> findMemberByEnName(String enName);
	Optional<Member> findMemberByName(String name);
}
