package com.github.bluekey.dto.artist;

import com.github.bluekey.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "트랙 추가할 시에 사용되는 간단한 아티스트 계정 정보")
public class SimpleArtistAccountDto {
    @Schema(description = "아티스트 ID", example = "1")
    private Long memberId;
    @Schema(description = "아티스트 이름(국문)", example = "혁기")
    private String name;
    @Schema(description = "아티스트 이름(영문)", example = "Hyukki")
    private String enName;

    @Schema(description = "요율", example = "30")
    private Integer commissionRate;

    @Builder
    public SimpleArtistAccountDto(Member member) {
        this.memberId = member.getId();
        this.name = member.getName();
        this.enName = member.getEnName();
        this.commissionRate = member.getCommissionRate();
    }
}
