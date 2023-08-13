package com.github.bluekey.controller.member;

import com.github.bluekey.dto.request.ArtistRequestDto;
import com.github.bluekey.dto.response.ArtistListReponseDto;
import io.swagger.v3.oas.annotations.Operation;
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
            @ApiResponse(responseCode = "200", description = "아티스트 정보 변경 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않는 아티스트ID 입니다.")
    })
    @PatchMapping("/profile")
    public void artistUpdate(@RequestBody ArtistRequestDto dto) {

    }

    @Operation(summary = "아티스트 등록" , description = "아티스트 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 등록 성공"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @PostMapping
    public void artistInsert(@RequestBody ArtistRequestDto dto) {

    }

    @Operation(summary = "관리자가 등록한 아티스트의 앨범 LIST", description = "관리자가 등록한 아티스트의 앨범 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관리자가 등록한 아티스트의 앨범 LIST 조회 완료"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @GetMapping("/{memberId}/albums")
    public ArtistListReponseDto getDashboard1(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        return null;
    }

//    // TODO dashboard List 조회 API 수정 가능성 있음.
//    @Operation(summary = "당월 아티스트가 트랙별 정산 내역", description = "당월 아티스트가 트랙별 정산 내역")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "당월 아티스트가 트랙별 정산 내역 조회 완료"),
//            @ApiResponse(responseCode = "400", description = "error")
//    })
//    @GetMapping("/{memberId}/dashboard/track")
//    public ArtistListReponseDto getDashboard2(
//            @RequestParam("monthly") LocalDate monthly,
//            @RequestParam("page") Integer page,
//            @RequestParam("size") Integer size,
//            @RequestParam("sortBy") String sortBy,
//            @RequestParam("searchBy") String searchBy,
//            @RequestParam("keyword") String keyword
//    ) {
//        return null;
//    }
//
//    @Operation(summary = "아티스트 기준 당월 TOP N 트랙 매출 비중 LIST", description = "아티스트 기준 당월 TOP N 트랙 매출 비중 LIST")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "아티스트 기준 당월 TOP N 트랙 매출 비중 LIST 조회 완료"),
//            @ApiResponse(responseCode = "400", description = "error")
//    })
//    @GetMapping("/{memberId}/dashboard/track")
//    public ArtistListReponseDto getDashboard3(
//            @RequestParam("monthly") LocalDate monthly,
//            @RequestParam("rank") Integer rank
//    ) {
//        return null;
//    }
//
//    @Operation(summary = "아티스트 대쉬보드 LIST", description = "아티스트 대쉬보드 LIST")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "아티스트 대쉬보드 조회 완료"),
//            @ApiResponse(responseCode = "400", description = "error")
//    })
//    @GetMapping("/{memberId}/dashboard")
//    public ArtistListReponseDto getDashboard4(
//            @RequestParam("monthly") LocalDate monthly
//    ) {
//        return null;
//    }

    // TODO dashboard List 조회 API 수정 가능성 있음.
    @Operation(summary = "아티스트 대쉬보드 LIST", description = "아티스트 대쉬보드 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 대쉬보드 조회 완료"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @GetMapping("/{memberId}/dashboard")
    public ArtistListReponseDto getDashboard(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ) {
        return null;
    }

    @Operation(summary = "Admin이 아티스트의 정보 변경" , description = "Admin이 아티스트의 정보 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin이 아티스트의 정보 변경 성공"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @PatchMapping("/{memberId}/profile")
    public void ArtistProfileUpdate(@RequestBody ArtistRequestDto dto) {

    }

    @Operation(summary = "아티스트 LIST 조회", description = "아티스트 LIST 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아티스트 LIST 조회 완료"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @GetMapping
    public ArtistListReponseDto getArtistProfileListDto(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam("monthly") LocalDate monthly
    ) {
        return null;
    }
}
