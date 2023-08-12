package com.github.bluekey.controller.member;

import com.github.bluekey.dto.response.ArtistsRevenueProportionReponseDto;
import com.github.bluekey.dto.response.DashboardTotalInfoResponseDto;
import com.github.bluekey.dto.response.MonthlyRevenueTrendResponseDto;
import com.github.bluekey.dto.response.TracksSettlementAmountResponseDto;
import com.github.bluekey.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
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
			@ApiResponse(responseCode = "400", description = "비정상적인 날짜", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("/dashboard/artist")
	public ArtistsRevenueProportionReponseDto getTopRevenueArtistsOfMonth(
			@RequestParam("monthly") LocalDate monthly, @RequestParam("rank") Integer rank
	) {
		return null;
	}

	@Operation(summary = "트랙별 정산 금액 리스트 (페이지네이션)", description = "트랙별 정산 금액 리스트 (페이지네이션)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정상 반환", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TracksSettlementAmountResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 값", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("/dashboard/track")
	public TracksSettlementAmountResponseDto getTracksSettlementAmount(
			@RequestParam("monthly") LocalDate monthly,
			@RequestParam("page") Integer page,
			@RequestParam("size") Integer size,
			@RequestParam("searchType") String searchType,
			@RequestParam("keyword") String keyword
	) {
		return null;
	}

	@Operation(summary = "대시보드에 보여질 정보", description = "대시보드에 보여질 정보")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정상 반환", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DashboardTotalInfoResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 값", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("/dashboard")
	public DashboardTotalInfoResponseDto getDashboardTotalInfo(
			@RequestParam("monthly") LocalDate monthly
	) {
		return null;
	}

	@Operation(summary = "월별 매출 추이", description = "월별 매출 추이")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "정상 반환", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MonthlyRevenueTrendResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 값", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
	})
	@GetMapping("/dashboard/trend")
	public MonthlyRevenueTrendResponseDto getMonthlyRevenueTrend(
			@RequestParam("startDate") LocalDate startDate,
			@RequestParam("endDate") LocalDate endDate
	) {
		return null;
	}
}
