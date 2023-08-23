package com.github.bluekey.controller.album;

import com.github.bluekey.dto.request.album.AlbumsRequestDto;
import com.github.bluekey.dto.response.album.AlbumIdResponseDto;
import com.github.bluekey.dto.response.album.AlbumListReponseDto;
import com.github.bluekey.dto.response.album.AlbumMonthlyAccontsReponseDto;
import com.github.bluekey.dto.response.album.AlbumResponseDto;
import com.github.bluekey.dto.response.album.AlbumSummaryResponseDto;
import com.github.bluekey.dto.response.album.AlbumTopReponseDto;
import com.github.bluekey.dto.response.album.AlbumTrackAccontsReponseDto;
import com.github.bluekey.dto.response.album.AlbumTrackListReponseDto;
import com.github.bluekey.dto.response.artist.ArtistAlbumsListReponseDto;
import com.github.bluekey.dto.response.transaction.OriginalTransactionResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Albums", description = "Album 관련 API")
@RestController
@RequestMapping("/api/v1/albums")
@RequiredArgsConstructor
public class AlbumController {

    @Operation(summary = "신규 앨범 등록" , description = "신규 앨범 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "새로운 앨범을 등록합니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumResponseDto.class)
                    )
            )
    })
    @PostMapping
    public AlbumResponseDto albumsInsert(@RequestBody AlbumsRequestDto dto) {
        return null;
    }

    @Operation(summary = "앨범정보 변경" , description = "앨범정보 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범정보 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumResponseDto.class)
                    )),
    })
    @PatchMapping("/{albumId}")
    public AlbumResponseDto albumsUpdate(@RequestBody AlbumsRequestDto dto) {
        return null;
    }

    @Operation(summary = "앨범 삭제", description = "앨범 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범정보 삭제 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumResponseDto.class)
                    ))
    })
    @DeleteMapping("/{albumId}")
    public AlbumIdResponseDto albumsDelete(@PathVariable("albumId") Long albumId) {
        return null;
    }

    @Operation(summary = "앨범의 당월 매출 TOP n 트랙 LIST 정보", description = "앨범의 당월 매출 TOP n 트랙 LIST 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumTopReponseDto.class)
                    )),
    })
    @GetMapping("/{albumId}/dashboard/topTrack")
    public AlbumTopReponseDto getAlbumTopList(
            @RequestParam("monthly") LocalDate monthly,
            @RequestParam("rank") Integer rank,
            @PathVariable("albumId") Long albumId
    ) {
        return null;
    }

    @Operation(summary = "앨범의 트랙별 정산 LIST", description = "앨범의 트랙별 정산 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumTrackAccontsReponseDto.class)
                    ))
    })
    @GetMapping("/{albumId}/dashboard/track")
    public AlbumTrackAccontsReponseDto getAlbumTrackAccontsList(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            @PathVariable("albumId") Long albumId
    ) {
        return null;
    }

    // TODO 앨범상세의 기본정보 가져오기 (보류)

    @Operation(summary = "앨범 대시보드에 들어갈 기본 정보", description = "앨범 대시보드에 들어갈 기본 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumSummaryResponseDto.class)
                    ))
    })
    @GetMapping("/{albumId}/dashboard/summary")
    public AlbumSummaryResponseDto getAlbumSummary(
            @PathVariable("albumId") Long albumId,
            @RequestParam("monthly") LocalDate monthly
    ) {
        return null;
    }


    @Operation(summary = "앨범의 월별 정산액 LIST", description = "앨범의 월별 정산액 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumMonthlyAccontsReponseDto.class)
                    ))
    })
    @GetMapping("/{albumId}/dashboard")
    public AlbumMonthlyAccontsReponseDto getAlbumMonthlyAccontsList(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            @PathVariable("albumId") Long albumId
    ) {
        return null;
    }

    @Operation(summary = "앨범의 트랙 리스트 조회", description = "앨범의 트랙 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범의 트랙 리스트 조회 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumTrackListReponseDto.class)
                    )),
    })
    @GetMapping("/{albumId}")
    public AlbumTrackListReponseDto getAlbumListDto(
            @PathVariable("albumId") Long albumId
    ) {
        return null;
    }

    // header 에 jwt 존재하는 ID 가져와서 조회 가능.
    // 아티스트면 본인 앨범만 조회, 어드민이면 전체 조회.
    // 최신 등록순.
    @Operation(summary = "앨범 리스트 조회", description = "앨범 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범 리스트 조회 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistAlbumsListReponseDto.class)
                    )),
    })
    @GetMapping
    public ArtistAlbumsListReponseDto getAlbumListDto(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam("keyword") String keyword
    ) {
        return null;
    }
}
