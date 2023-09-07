package com.github.bluekey.controller.member;

import static org.springframework.http.ResponseEntity.ok;

import com.github.bluekey.dto.artist.ArtistAccountDto;
import com.github.bluekey.dto.artist.ArtistProfileViewDto;
import com.github.bluekey.dto.filter.MonthlyTrackFilter;
import com.github.bluekey.dto.request.admin.AdminArtistProfileRequestDto;
import com.github.bluekey.dto.request.artist.ArtistProfileRequestDto;
import com.github.bluekey.dto.request.artist.ArtistRequestDto;
import com.github.bluekey.dto.response.admin.AdminArtistProfileListReponseDto;
import com.github.bluekey.dto.response.artist.*;
import com.github.bluekey.dto.response.auth.MemberIdResponseDto;
import com.github.bluekey.dto.response.common.MonthlyTrendResponseDto;
import com.github.bluekey.config.security.jwt.PrincipalConvertUtil;
import com.github.bluekey.service.auth.AuthService;
import com.github.bluekey.service.dashboard.BarChartDashboardService;
import com.github.bluekey.service.dashboard.MonthlyTracksDashBoardService;
import com.github.bluekey.service.dashboard.SummaryDashBoardService;
import com.github.bluekey.service.dashboard.TopTrackDashBoardService;
import com.github.bluekey.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Tag(name = "Artist", description = "Artist 관련 API")
@RestController
@RequestMapping("/api/v1/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final AuthService authService;
    private final MemberService memberService;
    private final TopTrackDashBoardService topTrackDashBoardService;
    private final SummaryDashBoardService summaryDashBoardService;
    private final BarChartDashboardService barChartDashboardService;
    private final MonthlyTracksDashBoardService monthlyTracksDashBoardService;

    @Operation(summary = "아티스트 정보 변경" , description = "아티스트 정보 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 정보 변경 성공"), })
    @PreAuthorize("hasRole('ARTIST')")
    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArtistProfileResponseDto> artistProfileUpdate(
            @Parameter(schema = @Schema(implementation = ArtistProfileRequestDto.class), description = "Artist 수정 API 입니다.")
            @RequestPart(value = "data", required = false) @Valid ArtistProfileRequestDto requestDto,
            @Parameter(description = "multipart/form-data 형식의 프로필 이미지 데이터, key 값은 file 입니다.")
            @RequestParam(value = "file", required = false) MultipartFile file) {
        return ok(memberService.updateArtistProfile(requestDto, file, PrincipalConvertUtil.getMemberId()));
    }

    @Operation(summary = "아티스트 등록" , description = "아티스트 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 등록 성공",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ArtistAccountDto.class))),
    })
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArtistAccountDto> createArtist(
            @Parameter(schema = @Schema(implementation = ArtistRequestDto.class), description = "Artist 등록 API 입니다.")
            @RequestPart("data") @Valid ArtistRequestDto requestDto,
            @Parameter(description = "multipart/form-data 형식의 프로필 이미지 데이터, key 값은 file 입니다.")
            @RequestParam(value = "file", required = false) MultipartFile file
            ) {
        return ok(authService.createArtist(file, requestDto));
    }

    @Deprecated
    @Operation(summary = "관리자가 등록한 아티스트의 앨범 LIST", description = "관리자가 등록한 아티스트의 앨범 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관리자가 등록한 아티스트의 앨범 LIST 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistAlbumsListResponseDto.class))),
    })
    @GetMapping("/{memberId}/albums")
    public ArtistAlbumsListResponseDto getArtistAlbums(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "keyword", required = false) String keyword,
            @PathVariable("memberId") Long memberId

    ) {
        return null;
    }

    @Operation(summary = "당월 아티스트의 트랙별 정산 내역", description = "당월 아티스트의 트랙별 정산 내역")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "당월 아티스트가 트랙별 정산 내역 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistMonthlyTrackListResponseDto.class))),
    })
    @GetMapping("/{memberId}/dashboard/track")
    public ResponseEntity<ArtistMonthlyTrackListResponseDto> getArtistMonthlyTracks(
            @RequestParam("monthly") String monthly,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @Parameter(description = "정렬 기준: revenue, netIncome, settlement, commissionRate") @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam("searchType") String searchType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @PathVariable("memberId") Long memberId,
            @Parameter(description = "검색할 아티스트 PK") @RequestParam(value="memberId", required = false) Long filteringMemberId,
            @Parameter(description = "매출액 시작 조건") @RequestParam(value="revenueFrom", required = false) Integer revenueFrom,
            @Parameter(description = "매출액 엔드 조건") @RequestParam(value="revenueTo", required = false) Integer revenueTo,
            @Parameter(description = "회사 이익 시작 조건") @RequestParam(value="netIncomeFrom", required = false) Integer netIncomeFrom,
            @Parameter(description = "회사 이익 엔드 조건") @RequestParam(value="netIncomeTo", required = false) Integer netIncomeTo,
            @Parameter(description = "정산액 시작 조건") @RequestParam(value="settlementFrom", required = false) Integer settlementFrom,
            @Parameter(description = "정산액 엔드 조건") @RequestParam(value = "settlementTo", required = false) Integer settlementTo,
            @Parameter(description = "요율 시작 조건") @RequestParam(value="commissionRateFrom", required = false) Integer commissionRateFrom,
            @Parameter(description = "요율 엔드 조건") @RequestParam(value="commissionRateTo", required = false) Integer commissionRateTo

    ) {
        Pageable pageable = PageRequest.of(page, size);
        MonthlyTrackFilter monthlyTrackFilter = MonthlyTrackFilter.builder()
                .memberId(filteringMemberId)
                .revenueFrom(revenueFrom)
                .revenueTo(revenueTo)
                .netIncomeFrom(netIncomeFrom)
                .netIncomeTo(netIncomeTo)
                .settlementFrom(settlementFrom)
                .settlementTo(settlementTo)
                .commissionRateFrom(commissionRateFrom)
                .commissionRateTo(commissionRateTo)
                .build();
        memberService.permissionCheck(memberId, PrincipalConvertUtil.getMemberId());
        return ResponseEntity.ok(monthlyTracksDashBoardService
                .getArtistTracks(monthly, pageable, sortBy, searchType, keyword, memberId, monthlyTrackFilter));
    }

    @Operation(summary = "아티스트 기준 당월 TOP N 트랙 매출 LIST", description = "아티스트 기준 당월 TOP N 트랙 매출 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 기준 당월 TOP N 트랙 매출 LIST 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistTopResponseDto.class))),
    })
    @GetMapping("/{memberId}/dashboard/top-track")
    public ResponseEntity<ArtistTopResponseDto> getArtistTop(
            @RequestParam("monthly") String monthly,
            @RequestParam("rank") Integer rank,
            @PathVariable("memberId") Long memberId
    ) {
        memberService.permissionCheck(memberId, PrincipalConvertUtil.getMemberId());
        return ResponseEntity.ok(topTrackDashBoardService.getArtistTopTracks(monthly, rank, memberId));
    }


    @Operation(summary = "아티스트 대쉬보드 기본정보", description = "아티스트 대쉬보드 기본정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 대쉬보드 조회 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistSummaryResponseDto.class))),
    })
    @GetMapping("/{memberId}/dashboard/summary")
    public ResponseEntity<ArtistSummaryResponseDto> getArtistDashboardSummary(
            @RequestParam("monthly") String monthly,
            @PathVariable("memberId") Long memberId
    ) {
        memberService.permissionCheck(memberId, PrincipalConvertUtil.getMemberId());
        return ResponseEntity.ok(summaryDashBoardService.getArtistDashboardInformation(monthly, memberId));
    }

    @Operation(summary = "아티스트 대쉬보드 월별 정산액 LIST", description = "아티스트 대쉬보드 월별 정산액 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 대쉬보드 월별 정산액 조회 완료",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistMonthlyAccountsResponseDto.class))),
    })
    @GetMapping("/{memberId}/dashboard/trend")
    public ResponseEntity<MonthlyTrendResponseDto> getArtistMonthlyAccounts(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @PathVariable("memberId") Long memberId
    ) {
        memberService.permissionCheck(memberId, PrincipalConvertUtil.getMemberId());
        return ok(barChartDashboardService.getBarChartDashboard(startDate, endDate, memberId));
    }

    @Operation(summary = "Admin이 아티스트의 정보 변경" , description = "Admin이 아티스트의 정보 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin이 아티스트의 정보 변경 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistAccountDto.class))),
    })
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @PatchMapping("/{memberId}/profile")
    public ResponseEntity<ArtistAccountDto> AdminArtistProfileUpdate(
            @RequestBody AdminArtistProfileRequestDto dto,
            @PathVariable("memberId") Long memberId
    ) {
        return ResponseEntity.ok(memberService.updateArtistProfileByAdmin(dto, memberId));
    }

    @Operation(summary = "아티스트 LIST 조회", description = "아티스트 LIST 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 LIST 조회 완료",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AdminArtistProfileListReponseDto.class))),
    })
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<AdminArtistProfileListReponseDto> getAdminArtistProfiles(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam("monthly") String monthly,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(memberService.getArtistsPagination(pageable, monthly, keyword));
    }

    @Operation(summary = "드롭다운 UI에서 사용되는 아티스트 리스트 조회", description = "트랙곡 추가할 시에 드롭다운 UI에서 사용되는 아티스트 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleArtistAccountListResponseDto.class))),
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @GetMapping("/simple")
    public ResponseEntity<SimpleArtistAccountListResponseDto> getSimpleArtists() {
        return ok(memberService.getSimpleArtistAccounts());
    }

    @Operation(summary = "아티스트 프로필 조회", description = "아티스트 프로필 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 프로필 조회 완료",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistProfileViewDto.class))),
    })
    @PreAuthorize("hasRole('ARTIST')")
    @GetMapping("/profile")
    public ResponseEntity<ArtistProfileViewDto> getArtistProfile() {
        return ok(memberService.getArtistProfile(PrincipalConvertUtil.getMemberId()));
    }

    @Operation(summary = "아티스트 프로필 이미지 삭제", description = "아티스트 프로필 이미지 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 프로필 이미지 삭제 완료")
    })
    @PreAuthorize("hasRole('ARTIST')")
    @DeleteMapping("/profile/image")
    public ResponseEntity<MemberIdResponseDto> deleteArtistProfileImage() {
        memberService.deleteArtistProfileImage(PrincipalConvertUtil.getMemberId());
        return ok(MemberIdResponseDto.builder().memberId(PrincipalConvertUtil.getMemberId()).build());
    }
}
