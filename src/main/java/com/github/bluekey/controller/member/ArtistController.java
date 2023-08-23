package com.github.bluekey.controller.member;

import com.github.bluekey.dto.artist.ArtistAccountDto;
import com.github.bluekey.dto.request.admin.AdminArtistProfileRequestDto;
import com.github.bluekey.dto.request.artist.ArtistProfileRequestDto;
import com.github.bluekey.dto.request.artist.ArtistRequestDto;
import com.github.bluekey.dto.response.admin.AdminArtistProfileListReponseDto;
import com.github.bluekey.dto.response.artist.ArtistAlbumsListResponseDto;
import com.github.bluekey.dto.response.artist.ArtistListResponseDto;
import com.github.bluekey.dto.response.artist.ArtistMonthlyAccountsReponseDto;
import com.github.bluekey.dto.response.artist.ArtistMonthlyTrackListReponseDto;
import com.github.bluekey.dto.response.artist.ArtistProfileResponseDto;
import com.github.bluekey.dto.response.artist.ArtistTopReponseDto;
import com.github.bluekey.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;

@Tag(name = "Artist", description = "Artist 관련 API")
@RestController
@RequestMapping("/api/v1/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final AuthService authService;

    @Operation(summary = "아티스트 정보 변경" , description = "아티스트 정보 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 정보 변경 성공"), })
    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArtistProfileResponseDto> artistProfileUpdate(
            @Parameter(schema = @Schema(implementation = ArtistProfileRequestDto.class), description = "Artist 수정 API 입니다.")
            @RequestPart("data") @Valid ArtistProfileRequestDto requestDto,
            @Parameter(description = "multipart/form-data 형식의 프로필 이미지 데이터, key 값은 file 입니다.")
            @RequestParam("file") MultipartFile file) {
        return null;
    }

    @Operation(summary = "아티스트 등록" , description = "아티스트 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 등록 성공",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ArtistAccountDto.class))),
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArtistAccountDto> createArtist(
            @Parameter(schema = @Schema(implementation = ArtistRequestDto.class), description = "Artist 등록 API 입니다.")
            @RequestPart("data") @Valid ArtistRequestDto requestDto,
            @Parameter(description = "multipart/form-data 형식의 프로필 이미지 데이터, key 값은 file 입니다.")
            @RequestParam("file") MultipartFile file
            ) {
        return ResponseEntity.ok(authService.createArtist(file, requestDto));
    }

    @Operation(summary = "관리자가 등록한 아티스트의 앨범 LIST", description = "관리자가 등록한 아티스트의 앨범 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관리자가 등록한 아티스트의 앨범 LIST 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistAlbumsListResponseDto.class))),
    })
    @GetMapping("/{memberId}/albums")
    public ArtistAlbumsListResponseDto getArtistAlbums(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @PathVariable("memberId") Long memberId
    ) {
        return null;
    }

    @Operation(summary = "당월 아티스트의 트랙별 정산 내역", description = "당월 아티스트의 트랙별 정산 내역")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "당월 아티스트가 트랙별 정산 내역 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistMonthlyTrackListReponseDto.class))),
    })
    @GetMapping("/{memberId}/dashboard/track")
    public ResponseEntity<ArtistMonthlyTrackListReponseDto> getArtistMonthlyTracks(
            @RequestParam("monthly") LocalDate monthly,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam("searchBy") String searchBy,
            @RequestParam(value = "keyword", required = false) String keyword,
            @PathVariable("memberId") Long memberId
    ) {
        return null;
    }

    @Operation(summary = "아티스트 기준 당월 TOP N 트랙 매출 LIST", description = "아티스트 기준 당월 TOP N 트랙 매출 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 기준 당월 TOP N 트랙 매출 LIST 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistTopReponseDto.class))),
    })
    @GetMapping("/{memberId}/dashboard/topTrack")
    public ResponseEntity<ArtistTopReponseDto> getArtistTop(
            @RequestParam("monthly") LocalDate monthly,
            @RequestParam("rank") Integer rank,
            @PathVariable("memberId") Long memberId
    ) {
        return null;
    }


    @Operation(summary = "아티스트 대쉬보드 기본정보", description = "아티스트 대쉬보드 기본정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 대쉬보드 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistListResponseDto.class))),
    })
    @GetMapping("/{memberId}/dashboard/summary")
    public ResponseEntity<ArtistListResponseDto> getArtistDashboardSummary(
            @RequestParam("monthly") String monthly,
            @PathVariable("memberId") Long memberId
    ) {
        return null;
    }

    @Operation(summary = "아티스트 대쉬보드 월별 정산액 LIST", description = "아티스트 대쉬보드 월별 정산액 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 대쉬보드 월별 정산액 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistMonthlyAccountsReponseDto.class))),
    })
    @GetMapping("/{memberId}/dashboard")
    public ResponseEntity<ArtistMonthlyAccountsReponseDto> getArtistMonthlyAccounts(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @PathVariable("memberId") Long memberId
    ) {
        return null;
    }

    @Operation(summary = "Admin이 아티스트의 정보 변경" , description = "Admin이 아티스트의 정보 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin이 아티스트의 정보 변경 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistAccountDto.class))),
    })
    @PatchMapping("/{memberId}/profile")
    public ResponseEntity<ArtistAccountDto> AdminArtistProfileUpdate(
            @RequestBody AdminArtistProfileRequestDto dto,
            @PathVariable("memberId") Long memberId
    ) {
        return null;
    }

    @Operation(summary = "아티스트 LIST 조회", description = "아티스트 LIST 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 LIST 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminArtistProfileListReponseDto.class))),
    })
    @GetMapping
    public ResponseEntity<AdminArtistProfileListReponseDto> getAdminArtistProfiles(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam("monthly") String monthly
    ) {
        return null;
    }
}
