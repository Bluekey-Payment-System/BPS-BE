package com.github.bluekey.controller.track;

import com.github.bluekey.dto.swagger.request.track.TrackRequestDto;
import com.github.bluekey.dto.swagger.response.track.TrackIdResponseDto;
import com.github.bluekey.dto.swagger.response.track.TrackResponseDto;
import com.github.bluekey.dto.swagger.response.transaction.OriginalTransactionResponseDto;
import com.github.bluekey.service.track.TrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;


@Tag(name = "Tracks", description = "Tracks 관련 API")
@RestController
@RequestMapping("/api/v1/tracks")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;

    @Operation(summary = "앨범의 트랙 등록" , description = "앨범의 트랙 등록")
    @ApiResponse(responseCode = "200",
            description = "해당 앨범에 트랙 등록",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OriginalTransactionResponseDto.class)
            )
    )
    @PostMapping("/albums/{albumId}")
    public ResponseEntity<TrackResponseDto> createTrack(
            @PathVariable("albumId") Long albumId,
            @RequestBody TrackRequestDto dto) {
        return ok(trackService.createTrack(albumId, dto));
    }

    // TODO: api url 수정 희망 -> albums/ 뒤에 trackId가 오는 것이 어색한 점.
    @Operation(summary = "트랙정보 변경" , description = "트랙정보 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "트랙정보 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrackResponseDto.class)
                    )),
    })
    @PatchMapping("/{trackId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<TrackResponseDto> updateTrack(
            @PathVariable("trackId") Long trackId,
            @RequestBody TrackRequestDto dto) {
        return ok(trackService.updateTrack(trackId, dto));
    }

    @Operation(summary = "트랙 삭제", description = "트랙 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수록곡 삭제 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrackIdResponseDto.class)
                    )),
    })
    @DeleteMapping("/{trackId}")
    public ResponseEntity<TrackIdResponseDto> removeTrack(@PathVariable("trackId") Long trackId) {
        Long removeTrackId = trackService.removeTrack(trackId);
        return ok(TrackIdResponseDto.builder().trackId(removeTrackId).build());
    }
}
