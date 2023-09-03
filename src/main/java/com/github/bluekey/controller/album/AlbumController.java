package com.github.bluekey.controller.album;

import com.github.bluekey.dto.album.NewAlbumInfoDto;
import com.github.bluekey.dto.response.album.AlbumIdResponseDto;
import com.github.bluekey.dto.response.album.AlbumMonthlyAccontsReponseDto;
import com.github.bluekey.dto.response.album.AlbumResponseDto;
import com.github.bluekey.dto.response.album.AlbumSummaryResponseDto;
import com.github.bluekey.dto.response.album.AlbumTopResponseDto;
import com.github.bluekey.dto.response.album.AlbumTrackAccountsResponseDto;
import com.github.bluekey.dto.response.album.AlbumTrackListResponseDto;
import com.github.bluekey.dto.response.artist.ArtistAlbumsListResponseDto;
import com.github.bluekey.dto.response.common.MonthlyTrendResponseDto;
import com.github.bluekey.jwt.PrincipalConvertUtil;
import com.github.bluekey.service.album.AlbumService;
import com.github.bluekey.service.dashboard.BarChartDashboardService;
import com.github.bluekey.service.dashboard.LineChartDashBoardService;
import com.github.bluekey.service.dashboard.SummaryDashBoardService;
import com.github.bluekey.service.dashboard.TopTrackDashBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Albums", description = "Album 관련 API")
@RestController
@RequestMapping("/api/v1/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    private final TopTrackDashBoardService topTrackDashBoardService;
    private final SummaryDashBoardService summaryDashBoardService;
    private final BarChartDashboardService barChartDashboardService;
    private final LineChartDashBoardService lineChartDashBoardService;

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
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponseDto> albumsInsert(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestPart("data") NewAlbumInfoDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(albumService.createAlbum(file, dto));
    }

    @Operation(summary = "앨범정보 변경" , description = "앨범정보 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범정보 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumResponseDto.class)
                    )),
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @PatchMapping(path = "/{albumId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlbumResponseDto> albumsUpdate(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "data", required = false) NewAlbumInfoDto dto,
            @PathVariable("albumId") Long albumId
    ) {
        return ResponseEntity.ok(albumService.updateAlbum(file, dto, albumId));
    }

    @Operation(summary = "앨범 삭제", description = "앨범 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범정보 삭제 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumResponseDto.class)
                    ))
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{albumId}")
    public ResponseEntity<AlbumIdResponseDto> albumsDelete(@PathVariable("albumId") Long albumId) {
        return ResponseEntity.ok(albumService.deleteAlbum(albumId));
    }

    @Operation(summary = "앨범의 당월 매출 TOP n 트랙 LIST 정보", description = "앨범의 당월 매출 TOP n 트랙 LIST 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumTopResponseDto.class)
                    )),
    })
    @GetMapping("/{albumId}/dashboard/top-track")
    public ResponseEntity<AlbumTopResponseDto> getAlbumTopList(
            @RequestParam("monthly") String monthly,
            @RequestParam("rank") Integer rank,
            @PathVariable("albumId") Long albumId
    ) {

        return ResponseEntity.ok(topTrackDashBoardService.getTopTracks(albumId, monthly, rank, PrincipalConvertUtil.getMemberId()));
    }

    @Operation(summary = "앨범의 트랙별 정산 LIST", description = "앨범의 트랙별 정산 LIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumTrackAccountsResponseDto.class)
                    ))
    })
    @GetMapping("/{albumId}/dashboard/tracks")
    public ResponseEntity<AlbumTrackAccountsResponseDto> getAlbumTrackAccounts(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @PathVariable("albumId") Long albumId
    ) {
        return ResponseEntity.ok(lineChartDashBoardService
                .getAlbumLineChartDashboard(startDate, endDate, albumId, PrincipalConvertUtil.getMemberId()));
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
    public ResponseEntity<AlbumSummaryResponseDto> getAlbumSummary(
            @PathVariable("albumId") Long albumId,
            @RequestParam("monthly") String monthly
    ) {
        return ResponseEntity.ok(summaryDashBoardService.getAlbumSummary(albumId, monthly, PrincipalConvertUtil.getMemberId()));
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
    public ResponseEntity<MonthlyTrendResponseDto> getAlbumMonthlyAccounts(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @PathVariable("albumId") Long albumId
    ) {
        return ResponseEntity.ok(
                barChartDashboardService.getAlbumBarChartDashboard(startDate, endDate,
                albumId, PrincipalConvertUtil.getMemberId()));
    }

    @Operation(summary = "앨범의 트랙 리스트 조회", description = "앨범의 트랙 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범의 트랙 리스트 조회 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AlbumTrackListResponseDto.class)
                    )),
    })
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumTrackListResponseDto> getAlbum(
            @PathVariable("albumId") Long albumId
    ) {
        return ResponseEntity.ok(albumService.getAlbumTrackList(albumId));
    }

    @Operation(summary = "앨범 리스트 조회", description = "앨범 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범 리스트 조회 완료",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistAlbumsListResponseDto.class)
                    )),
    })
    @GetMapping
    public ResponseEntity<ArtistAlbumsListResponseDto> getAlbums(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword == null) {
            return ResponseEntity.ok(albumService.getAlbums(pageable, PrincipalConvertUtil.getMemberId()));
        } else {
            return ResponseEntity.ok(albumService.getAlbums(pageable, PrincipalConvertUtil.getMemberId(), keyword));
        }
    }
}
