package com.github.bluekey.service.notification;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.notification.MemberRequestAuthority;
import com.github.bluekey.entity.notification.RequestAuthority;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.notification.MemberRequestAuthorityRepository;
import com.github.bluekey.repository.notification.RequestAuthorityRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestAuthorityService {

	private final MemberRepository memberRepository;
	private final RequestAuthorityRepository requestAuthorityRepository;
	private final MemberRequestAuthorityRepository memberRequestAuthorityRepository;

	/**
	 * 새로 가입한 admin이 SUPER_ADMIN에게 권한 요청
	 *
	 * @param senderId 새로 가입한 권한 요청자 id
	 */
	@Transactional
	public void requestAuthority(Long senderId) {
		List<Member> superAdmins = memberRepository.findMemberByRoleAndIsRemovedFalse(MemberRole.SUPER_ADMIN);

		RequestAuthority requestAuthority = RequestAuthority.builder()
				.senderId(senderId)
				.build();
		RequestAuthority savedRequestAuthority = requestAuthorityRepository.save(requestAuthority);

		List<MemberRequestAuthority> memberRequestAuthorities = superAdmins.stream()
				.map(superAdmin -> MemberRequestAuthority.builder()
						.member(superAdmin)
						.requestAuthority(savedRequestAuthority)
						.build())
				.collect(Collectors.toList());
		memberRequestAuthorityRepository.saveAll(memberRequestAuthorities);
	}
}
