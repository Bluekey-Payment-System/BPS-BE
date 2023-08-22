package com.github.bluekey.service.member;

import com.github.bluekey.dto.ArtistAccountDto;
import com.github.bluekey.dto.request.ArtistRequestDto;
import com.github.bluekey.dto.request.SignupRequestDto;
import com.github.bluekey.dto.response.SignupResponseDto;
import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberType;
import com.github.bluekey.exception.BusinessException;
import com.github.bluekey.exception.ErrorCode;
import com.github.bluekey.repository.member.MemberRepository;
import com.github.bluekey.s3.manager.AwsS3Manager;
import com.github.bluekey.s3.manager.S3PrefixType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

}
