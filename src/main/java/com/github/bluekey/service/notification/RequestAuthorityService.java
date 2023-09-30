package com.github.bluekey.service.notification;

import com.github.bluekey.dto.common.ListResponse;
import com.github.bluekey.dto.common.MemberBase;
import com.github.bluekey.dto.response.RequestAuthorityResponse;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.notification.MemberRequestAuthority;
import com.github.bluekey.entity.notification.RequestAuthority;
import com.github.bluekey.entity.notification.RequestStatus;
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
		List<Member> superAdmins = memberRepository.findMembersByRoleAndIsRemovedFalse(MemberRole.SUPER_ADMIN);

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

	@Transactional
	public void approveAuthority(Long loginUserId, Long requestAuthorityId) {
		// requestAuthorityId가 존재하는지 확인
		RequestAuthority requestAuthority = requestAuthorityRepository
				.findRequestAuthorityByIdAndStatusOrElseThrow(requestAuthorityId, RequestStatus.PENDING);

		// memberRequestAuthorities에 loginUserId가 존재하는지 확인
		memberRequestAuthorityRepository.findMemberRequestAuthorityByMemberIdAndRequestAuthorityIdOrElseThrow(loginUserId, requestAuthorityId);

		// MemberRole을 ADMIN으로 변경
		Member sender = memberRepository.findMemberByIdAndIsRemovedFalseOrElseThrow(requestAuthority.getSenderId());
		sender.updateRole(MemberRole.ADMIN);

		// requestAuthority의 상태를 ACCEPTED로 변경
		requestAuthority.confirm(RequestStatus.ACCEPTED);

		memberRepository.save(sender);
		requestAuthorityRepository.save(requestAuthority);
	}

	@Transactional
	public void rejectAuthority(Long loginUserId, Long requestAuthorityId) {
		// requestAuthorityId가 존재하는지 확인
		RequestAuthority requestAuthority = requestAuthorityRepository
				.findRequestAuthorityByIdAndStatusOrElseThrow(requestAuthorityId, RequestStatus.PENDING);

		// memberRequestAuthorities에 loginUserId가 존재하는지 확인
		memberRequestAuthorityRepository.findMemberRequestAuthorityByMemberIdAndRequestAuthorityIdOrElseThrow(loginUserId, requestAuthorityId);

		// requestAuthority의 상태를 REJECTED로 변경
		requestAuthority.confirm(RequestStatus.REJECTED);

		requestAuthorityRepository.save(requestAuthority);
	}

	@Transactional(readOnly = true)
	public ListResponse<RequestAuthorityResponse> getRequestAuthority(Long loginUserId) {

		Member loginMember = memberRepository.findMemberByIdAndIsRemovedFalseOrElseThrow(loginUserId);

		List<RequestAuthorityResponse> requestAuthorities = loginMember.getMemberRequestAuthorities().stream()
				.filter(memberRequestAuthority -> memberRequestAuthority.getRequestAuthority().getStatus() == RequestStatus.PENDING)
				.map(memberRequestAuthority -> {
					Member sender = memberRepository
							.findMemberByIdAndIsRemovedFalseOrElseThrow(memberRequestAuthority.getRequestAuthority().getSenderId());
					return RequestAuthorityResponse.builder()
							.requestAuthorityId(memberRequestAuthority.getRequestAuthority().getId())
							.sender(MemberBase.from(sender))
							.createdAt(memberRequestAuthority.getRequestAuthority().getCreatedAt())
							.build();
				})
				.collect(Collectors.toList());

		return new ListResponse<>(requestAuthorities);
	}
}
