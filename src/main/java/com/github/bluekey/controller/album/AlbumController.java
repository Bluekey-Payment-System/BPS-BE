package com.github.bluekey.controller.album;

import com.github.bluekey.dto.request.AlbumsRequestDto;
import com.github.bluekey.dto.response.*;
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
            @ApiResponse(responseCode = "200",
                    description = "리스트 형태로 정상적으로 업로드가 완료된 엑셀 파일 내역중 해당 pk에 해당하는 인스턴스를 삭제하고 반환합니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OriginalTransactionResponseDto.class)
                    )
            )
    })
    @PostMapping
    public void albumsInsert(@RequestBody AlbumsRequestDto dto) {

    }

    @Operation(summary = "앨범정보 변경" , description = "앨범정보 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범정보 변경 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않는 앨범ID 입니다.")
    })
    @PatchMapping("/{albumId}")
    public void albumsUpdate(@RequestBody AlbumsRequestDto dto) {

    }

    @Operation(summary = "앨범 삭제", description = "앨범 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범정보 삭제 완료"),
            @ApiResponse(responseCode = "400", description = "유효하지 않는 앨범ID 입니다.")
    })
    @DeleteMapping("/{albumId}")
    public void albumsDelete(@PathVariable("albumId") Long albumId) {

    }

    @Operation(summary = "앨범의 당월 매출 TOP n 트랙 LIST 정보", description = "앨범의 당월 매출 TOP n 트랙 LIST 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 완료"),
            @ApiResponse(responseCode = "400", description = "error", content = {})
    })
    @GetMapping("/{albumId}/dashboard/topTrack")
    public AlbumTopReponseDto getAlbumTopList(
            @RequestParam("monthly") LocalDate monthly,
            @RequestParam("rank") Integer rank
    ) {
        return null;
    }

    @Operation(summary = "앨범의 트랙별 정산 LIST", description = "앨범의 트랙별 정산 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 완료"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @GetMapping("/{albumId}/dashboard/track")
    public AlbumTrackAccontsReponseDto getAlbumTrackAccontsList(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ) {
        return null;
    }

    // TODO 앨범상세의 기본정보 가져오기 (보류)

    @Operation(summary = "앨범의 월별 정산액 LIST", description = "앨범의 월별 정산액 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 완료"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @GetMapping("/{albumId}/dashboard")
    public AlbumMonthlyAccontsReponseDto getAlbumMonthlyAccontsList(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ) {
        return null;
    }

    @Operation(summary = "앨범의 트랙 리스트 조회", description = "앨범의 트랙 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범의 트랙 리스트 조회 완료"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @GetMapping("/{albumId}")
    public AlbumListReponseDto getAlbumListDto(
            @RequestParam("albumId") String albumId
    ) {
        return null;
    }

    // header 에 jwt 존재하는 ID 가져와서 조회 가능.
    // 아티스트면 본인 앨범만 조회, 어드민이면 전체 조회.
    // 최신 등록순.
    @Operation(summary = "앨범 리스트 조회", description = "앨범 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범 리스트 조회 완료"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @GetMapping
    public ArtistAlbumsListReponseDto getArtistAlbumListDto(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam("keyword") String keyword
    ) {
        return null;
    }
}
