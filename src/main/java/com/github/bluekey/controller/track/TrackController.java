package com.github.bluekey.controller.track;

import com.github.bluekey.dto.response.track.TrackResponseDto;
import com.github.bluekey.dto.response.transaction.OriginalTransactionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Tracks", description = "Tracks 관련 API")
@RestController
@RequestMapping("/api/v1/tracks")
@RequiredArgsConstructor
public class TrackController {

    @Operation(summary = "앨범의 트랙 등록" , description = "앨범의 트랙 등록")
    @ApiResponse(responseCode = "200",
            description = "리스트 형태로 정상적으로 업로드가 완료된 엑셀 파일 내역중 해당 pk에 해당하는 인스턴스를 삭제하고 반환합니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OriginalTransactionResponseDto.class)
            )
    )
    @PostMapping("/{albumId}/tracks")
    public void tracksInsert(@RequestBody TrackResponseDto dto) {

    }

    @Operation(summary = "수록곡 삭제", description = "수록곡 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수록곡 삭제 완료"),
            @ApiResponse(responseCode = "400", description = "유효하지 않는 수록곡ID 입니다.")
    })
    @DeleteMapping("/{trackId}")
    public void trackDelete(@PathVariable("trackId") Long albumId) {

    }
}
