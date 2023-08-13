package com.github.bluekey.controller.track;

import com.github.bluekey.dto.request.TrackRequestDto;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "수록곡 등록" , description = "수록곡 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수록곡 등록 완료"),
            @ApiResponse(responseCode = "400", description = "유효하지 않는 앨범ID 입니다.")
    })
    @PatchMapping("/{trackId}")
    public void trackInsert(@RequestBody TrackRequestDto dto) {

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
