package com.github.bluekey.repository.member;

import com.github.bluekey.entity.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);
	Optional<Member> findByLoginId(String loginId);

	Optional<Member> findMemberByEnName(String enName);
	Optional<Member> findMemberByName(String name);
}
