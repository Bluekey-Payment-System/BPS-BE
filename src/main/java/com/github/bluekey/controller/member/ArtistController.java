package com.github.bluekey.controller.member;

import com.github.bluekey.dto.response.*;
import com.github.bluekey.dto.request.AdminArtistProfileRequestDto;
import com.github.bluekey.dto.request.ArtistProfileRequestDto;
import com.github.bluekey.dto.request.ArtistRequestDto;
import com.github.bluekey.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Artist", description = "Artist 관련 API")
@RestController
@RequestMapping("/api/v1/artist")
@RequiredArgsConstructor
public class ArtistController {
    @Operation(summary = "아티스트 정보 변경" , description = "아티스트 정보 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 정보 변경 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않는 아티스트ID 입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("/profile")
    public void artistProfileUpdate(@RequestBody ArtistProfileRequestDto dto) {

    }

    @Operation(summary = "아티스트 등록" , description = "아티스트 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
            @ApiResponse(responseCode = "400", description = "error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping
    public void artistInsert(@RequestBody ArtistRequestDto dto) {

    }

    @Operation(summary = "관리자가 등록한 아티스트의 앨범 LIST", description = "관리자가 등록한 아티스트의 앨범 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관리자가 등록한 아티스트의 앨범 LIST 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistAlbumsListReponseDto.class))),
            @ApiResponse(responseCode = "400", description = "error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{memberId}/albums")
    public ArtistAlbumsListReponseDto getArtistAlbumsList(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        return null;
    }

    // TODO: 임의로 end point를 나눴지만 다시 협의하여 바꾸는 것이 좋아보임.
    @Operation(summary = "당월 아티스트의 트랙별 정산 내역", description = "당월 아티스트의 트랙별 정산 내역")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "당월 아티스트가 트랙별 정산 내역 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistMonthlyTrackListReponseDto.class))),
            @ApiResponse(responseCode = "400", description = "error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{memberId}/dashboard/settlement/track")
    public ArtistMonthlyTrackListReponseDto getArtistMonthlyTrackList(
            @RequestParam("monthly") LocalDate monthly,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("searchBy") String searchBy,
            @RequestParam("keyword") String keyword
    ) {
        return null;
    }

    // TODO: 임의로 end point를 나눴지만 다시 협의하여 바꾸는 것이 좋아보임.
    @Operation(summary = "아티스트 기준 당월 TOP N 트랙 매출 LIST", description = "아티스트 기준 당월 TOP N 트랙 매출 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 기준 당월 TOP N 트랙 매출 LIST 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistTopReponseDto.class))),
            @ApiResponse(responseCode = "400", description = "error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{memberId}/dashboard/rank/track")
    public ArtistTopReponseDto getArtistTop(
            @RequestParam("monthly") LocalDate monthly,
            @RequestParam("rank") Integer rank
    ) {
        return null;
    }


    @Operation(summary = "아티스트 대쉬보드 기본정보", description = "아티스트 대쉬보드 기본정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 대쉬보드 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistListReponseDto.class))),
            @ApiResponse(responseCode = "400", description = "error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{memberId}/dashboard")
    public ArtistListReponseDto getArtistList(
            @RequestParam("monthly") LocalDate monthly
    ) {
        return null;
    }

    // TODO: 임의로 end point를 나눴지만 다시 협의하여 바꾸는 것이 좋아보임.
    @Operation(summary = "아티스트 대쉬보드 월별 정산액 LIST", description = "아티스트 대쉬보드 월별 정산액 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 대쉬보드 월별 정산액 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistMonthlyAccountsReponseDto.class))),
            @ApiResponse(responseCode = "400", description = "error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{memberId}/dashboard/monthly")
    public ArtistMonthlyAccountsReponseDto getArtistMonthlyAccounts(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ) {
        return null;
    }

    @Operation(summary = "Admin이 아티스트의 정보 변경" , description = "Admin이 아티스트의 정보 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin이 아티스트의 정보 변경 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))),
            @ApiResponse(responseCode = "400", description = "error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("/{memberId}/profile")
    public void AdminArtistProfileUpdate(@RequestBody AdminArtistProfileRequestDto dto) {

    }

    @Operation(summary = "아티스트 LIST 조회", description = "아티스트 LIST 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 LIST 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminArtistProfileListReponseDto.class))),
            @ApiResponse(responseCode = "400", description = "error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping
    public AdminArtistProfileListReponseDto getAdminArtistProfileListDto(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam("monthly") LocalDate monthly
    ) {
        return null;
    }
}
