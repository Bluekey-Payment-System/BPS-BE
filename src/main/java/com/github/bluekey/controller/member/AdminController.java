package com.github.bluekey.controller.member;

import com.github.bluekey.dto.response.artist.ArtistsRevenueProportionReponseDto;
import com.github.bluekey.dto.response.common.DashboardTotalInfoResponseDto;
import com.github.bluekey.dto.response.common.MonthlyRevenueTrendResponseDto;
import com.github.bluekey.dto.response.track.TracksSettlementAmountResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "admin", description = "관리자")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

	@Operation(summary = "월별 Top n 아티스트 매출액과 비율", description = "월별 Top n 아티스트 매출액과 비율")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정상 반환", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistsRevenueProportionReponseDto.class))),
	})
	@GetMapping("/dashboard/artist")
	public ArtistsRevenueProportionReponseDto getTopRevenueArtistsOfMonth(
			@Parameter(description = "정보를 얻고 싶은 월 (format: yyyy-MM)") @RequestParam("monthly") LocalDate monthly,
			@Parameter(description = "Top 5위까지 알고 싶은 경우 rank=5") @RequestParam("rank") Integer rank
	) {
		return null;
	}

	@Operation(summary = "트랙별 정산 금액 리스트 (페이지네이션)", description = "트랙별 정산 금액 리스트 (페이지네이션)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정상 반환", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TracksSettlementAmountResponseDto.class))),
	})
	@GetMapping("/dashboard/track")
	public TracksSettlementAmountResponseDto getTracksSettlementAmount(
			@Parameter(description = "정보를 얻고 싶은 월 (format: yyyy-MM)") @RequestParam("monthly") LocalDate monthly,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size,
			@Parameter(description = "검색 타입 곡명 or 앨범명") @RequestParam("searchType") String searchType,
			@Parameter(description = "검색할 키워드") @RequestParam("keyword") String keyword
	) {
		return null;
	}

	@Operation(summary = "대시보드에 보여질 정보", description = "대시보드에 보여질 정보")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정상 반환", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DashboardTotalInfoResponseDto.class))),
	})
	@GetMapping("/dashboard")
	public DashboardTotalInfoResponseDto getDashboardTotalInfo(
			@Parameter(description = "정보를 얻고 싶은 월 (format: yyyy-MM)") @RequestParam("monthly") LocalDate monthly
	) {
		return null;
	}

	@Operation(summary = "월별 매출 추이", description = "월별 매출 추이")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정상 반환", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MonthlyRevenueTrendResponseDto.class))),
	})
	@GetMapping("/dashboard/trend")
	public MonthlyRevenueTrendResponseDto getMonthlyRevenueTrend(
			@Parameter(description = "월별 추이의 시작일 (format: yyyy-MM)") @RequestParam("startDate") LocalDate startDate,
			@Parameter(description = "월별 추이의 종료일 (format: yyyy-MM)") @RequestParam("endDate") LocalDate endDate
	) {
		return null;
	}
}
