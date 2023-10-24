package com.github.bluekey.dto.response;

import com.github.bluekey.entity.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestAuthorityUpdateResponseDto {
    private String nickName;
    private String loginId;

    @Builder
    private RequestAuthorityUpdateResponseDto(final String nickName, final String loginId) {
        this.nickName = nickName;
        this.loginId = loginId;
    }

    public static RequestAuthorityUpdateResponseDto from(Member member) {
        return RequestAuthorityUpdateResponseDto.builder()
                .nickName(member.getName())
                .loginId(member.getLoginId())
                .build();
    }
}
