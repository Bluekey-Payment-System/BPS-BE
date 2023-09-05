package com.github.bluekey.repository.member;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import java.util.List;
import java.util.Optional;

import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.bluekey.entity.member.MemberType;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findMemberByLoginId(String loginId);
	Optional<Member> findMemberByEnName(String enName);
	Optional<Member> findMemberByName(String name);
	Optional<Member> findMemberByIdAndIsRemovedFalse(Long id);

	// Type 조건에 따른 Member 조회
	@Query("select m from Member m where m.email.value = :email and m.type = :type")
	Optional<Member> findMemberByEmailAndType(@Param("email") String email, @Param("type") MemberType type);
	Optional<Member> findMemberByNameAndType(String name, MemberType type);
	Optional<Member> findMemberByEnNameAndType(String enName, MemberType type);

	// TODO: isRemoved 조건 추가
	Page<Member> findMembersByType(MemberType type, PageRequest pageable);
	Page<Member> findMembersByRole(MemberRole role, PageRequest pageable);

	List<Member> findMemberByRoleAndIsRemovedFalse(MemberRole role);
	List<Member> findMembersByRoleAndIsRemovedFalseAndNameContainingIgnoreCaseOrEnNameContainingIgnoreCase(MemberRole role, String name, String enName);

	default Member findByIdOrElseThrow(Long id) {
		return this.findById(id).orElseThrow(() ->
				new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
	}
}
