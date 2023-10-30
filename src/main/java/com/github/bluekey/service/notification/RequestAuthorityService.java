package com.github.bluekey.service.notification;

import com.github.bluekey.dto.common.AdminBase;
import com.github.bluekey.dto.common.ListResponse;
import com.github.bluekey.dto.response.RequestAuthorityPendingStatusResponseDto;
import com.github.bluekey.dto.response.RequestAuthorityResponse;
import com.github.bluekey.dto.response.RequestAuthorityUpdateResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.notification.MemberRequestAuthority;
import com.github.bluekey.entity.notification.RequestAuthority;
import com.github.bluekey.entity.notification.RequestStatus;
import com.github.bluekey.exception.AuthenticationException;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.repository.notification.MemberRequestAuthorityRepository;
import com.github.bluekey.repository.notification.RequestAuthorityRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
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

		Member sender = memberRepository.findMemberByIdAndIsRemovedFalseOrElseThrow(senderId);

		if (sender.getRejectCount() >= 5) {
			throw new BusinessException(ErrorCode.AUTHENTICATION_BANNED);
		}

		List<MemberRequestAuthority> memberRequestAuthorities = superAdmins.stream()
				.map(superAdmin -> MemberRequestAuthority.builder()
						.member(superAdmin)
						.requestAuthority(savedRequestAuthority)
						.build())
				.collect(Collectors.toList());
		memberRequestAuthorityRepository.saveAll(memberRequestAuthorities);
	}

	@Transactional
	public RequestAuthorityUpdateResponseDto approveAuthority(Long loginUserId, Long requestAuthorityId) {
		// requestAuthorityId가 존재하는지 확인
		RequestAuthority requestAuthority = requestAuthorityRepository
				.findRequestAuthorityByIdAndStatusOrElseThrow(requestAuthorityId, RequestStatus.PENDING);

		// memberRequestAuthorities에 loginUserId가 존재하는지 확인
		memberRequestAuthorityRepository.findMemberRequestAuthorityByMemberIdAndRequestAuthorityIdOrElseThrow(loginUserId, requestAuthorityId);

		// MemberRole을 ADMIN으로 변경
		Member sender = memberRepository.findMemberByIdAndIsRemovedFalseOrElseThrow(requestAuthority.getSenderId());
		sender.updateRole(MemberRole.ADMIN);

		// requestAuthority의 상태를 APPROVED로 변경
		requestAuthority.confirm(RequestStatus.APPROVED);

		memberRepository.save(sender);
		requestAuthorityRepository.save(requestAuthority);
		return RequestAuthorityUpdateResponseDto.from(sender);
	}

	@Transactional
	public RequestAuthorityUpdateResponseDto rejectAuthority(Long loginUserId, Long requestAuthorityId) {
		// requestAuthorityId가 존재하는지 확인
		RequestAuthority requestAuthority = requestAuthorityRepository
				.findRequestAuthorityByIdAndStatusOrElseThrow(requestAuthorityId, RequestStatus.PENDING);

		// memberRequestAuthorities에 loginUserId가 존재하는지 확인
		memberRequestAuthorityRepository.findMemberRequestAuthorityByMemberIdAndRequestAuthorityIdOrElseThrow(loginUserId, requestAuthorityId);

		// requestAuthority의 상태를 REJECTED로 변경
		requestAuthority.confirm(RequestStatus.REJECTED);

		// request authority 요청 주체
		Long senderId = requestAuthority.getSenderId();
		Member sender = memberRepository.findMemberByIdAndIsRemovedFalseOrElseThrow(senderId);

		sender.updateRole(MemberRole.REJECTED);
		sender.reject();

		memberRepository.save(sender);

		requestAuthorityRepository.save(requestAuthority);
		return RequestAuthorityUpdateResponseDto.from(sender);
	}

	@Transactional(readOnly = true)
	public ListResponse<RequestAuthorityResponse> getRequestAuthority(Long loginUserId) {

		Member loginMember = memberRepository.findMemberByIdAndIsRemovedFalseOrElseThrow(loginUserId);

		List<RequestAuthorityResponse> requestAuthorities = loginMember.getMemberRequestAuthorities().stream()
				.filter(memberRequestAuthority ->
						memberRequestAuthority.getRequestAuthority().getStatus() == RequestStatus.PENDING
								|| LocalDateTime.now().minusWeeks(1).isBefore(memberRequestAuthority.getRequestAuthority().getConfirmAt()))
				.map(memberRequestAuthority -> {
					Member sender = memberRepository
							.findMemberByIdAndIsRemovedFalseOrElseThrow(memberRequestAuthority.getRequestAuthority().getSenderId());
					return RequestAuthorityResponse.builder()
							.requestAuthorityId(memberRequestAuthority.getRequestAuthority().getId())
							.sender(AdminBase.from(sender))
							.status(memberRequestAuthority.getRequestAuthority().getStatus())
							.createdAt(memberRequestAuthority.getRequestAuthority().getCreatedAt())
							.build();
				})
				.sorted(Comparator.comparing((RequestAuthorityResponse requestAuthorityResponse)
								-> requestAuthorityResponse.getStatus() == RequestStatus.PENDING ? 1 : 0)
						.thenComparing(RequestAuthorityResponse::getCreatedAt).reversed())
				.collect(Collectors.toList());

		return new ListResponse<>(requestAuthorities.size(), requestAuthorities);
	}

	@Transactional(readOnly = true)
	public RequestAuthorityPendingStatusResponseDto hasPendingRequestAuthority(Long loginUserId) {
		Member loginMember = memberRepository.findMemberByIdAndIsRemovedFalseOrElseThrow(loginUserId);
		boolean hasPendingRequestAuthority = loginMember.getMemberRequestAuthorities().stream().anyMatch(request -> request.getRequestAuthority().getStatus().equals(RequestStatus.PENDING));
		return new RequestAuthorityPendingStatusResponseDto(hasPendingRequestAuthority);
	}
}
