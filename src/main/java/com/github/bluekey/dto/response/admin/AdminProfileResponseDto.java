package com.github.bluekey.dto.response.admin;

import com.github.bluekey.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "어드민 프로필 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminProfileResponseDto {

    @Schema(description = "어드민 PK", example = "1")
    private Long memberId;
    @Schema(description = "어드민 역할", example = "SUPER_ADMIN")
    private String role;
    @Schema(description = "어드민 닉네임", example = "admin")
    private String nickname;
    @Schema(description = "로그인 아이디", example = "qwerty123")
    private String loginId;
    @Schema(description = "이메일", example = "bluekey@gmail.com")
    private String email;
    @Schema(description = "프로필 이미지 url", example = "https://s3~~")
    private String profileImage;

    @Builder
    private AdminProfileResponseDto(Long memberId, String role, String nickname, String loginId, String email, String profileImage) {
        this.memberId = memberId;
        this.role = role;
        this.nickname = nickname;
        this.loginId = loginId;
        this.email = email;
        this.profileImage = profileImage;
    }

    public static AdminProfileResponseDto from(Member admin) {
        return AdminProfileResponseDto.builder()
                .memberId(admin.getId())
                .role(admin.getRole().name())
                .nickname(admin.getName())
                .loginId(admin.getLoginId())
                .email(admin.getEmail().getValue())
                .profileImage(admin.getProfileImage())
                .build();
    }
}
