package com.github.bluekey.repository.member;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.member.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findMemberByLoginId(String loginId);
	Optional<Member> findMemberByEnName(String enName);
	Optional<Member> findMemberByName(String name);

	// Type 조건에 따른 Member 조회
	Optional<Member> findMemberByEmailAndType(String email, MemberType type);
}
