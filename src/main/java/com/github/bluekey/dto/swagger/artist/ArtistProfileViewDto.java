package com.github.bluekey.dto.swagger.artist;

import com.github.bluekey.entity.member.Member;
import com.github.bluekey.entity.member.MemberRole;
import com.github.bluekey.entity.member.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "아티스트가 프로필 확인할 때 보여질 정보")
public class ArtistProfileViewDto {
    @Schema(description = "아티스트 ID", example = "1")
    private Long memberId;
    @Schema(description = "아티스트 이름(국문)", example = "혁기")
    private String name;
    @Schema(description = "아티스트 이름(영문)", example = "Hyukki")
    private String enName;
    @Schema(description = "로그인 아이디", example = "qwerty123")
    private String loginId;
    @Schema(description = "이메일", example = "bluekey@gmail.com")
    private String email;
    @Schema(description = "멤버 타입", example = "USER")
    private MemberType type;
    @Schema(description = "멤버 권한", example = "ARTIST")
    private MemberRole role;
    @Schema(description = "프로필 이미지", example = "artist.jpg")
    private String profileImage;

    @Builder
    private ArtistProfileViewDto(final Long memberId, final String name, final String enName,
                                 final String loginId, final String email, final MemberType type, final MemberRole role,
                                 final String profileImage) {
        this.memberId = memberId;
        this.name = name;
        this.enName = enName;
        this.loginId = loginId;
        this.email = email;
        this.type = type;
        this.role = role;
        this.profileImage = profileImage;
    }

    public static ArtistProfileViewDto from(Member member) {
        return ArtistProfileViewDto.builder()
                .memberId(member.getId())
                .name(member.getName())
                .enName(member.getEnName())
                .loginId(member.getLoginId())
                .email(member.getEmail().getValue())
                .type(member.getType())
                .role(member.getRole())
                .profileImage(member.getProfileImage())
                .build();
    }
}
