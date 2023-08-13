package com.github.bluekey.controller.album;

import com.github.bluekey.dto.request.AlbumsRequestDto;
import com.github.bluekey.dto.response.AlbumListReponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Album", description = "Album 관련 API")
@RestController
@RequestMapping("/api/v1/album")
@RequiredArgsConstructor
public class AlbumController {

    @Operation(summary = "신규 앨범 등록" , description = "신규 앨범 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신규 앨범 등록 성공"),
            @ApiResponse(responseCode = "400", description = "error")
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

    // TODO dashboard List 조회 API 수정 가능성 있음.
    @Operation(summary = "앨범의 당월 매출 TOP n 트랙 LIST 정보", description = "앨범의 당월 매출 TOP n 트랙 LIST 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보 조회 완료"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @GetMapping("/{albumId}/dashboard/track")
    public AlbumListReponseDto getAlbumList(
            @RequestParam("monthly") LocalDate monthly, @RequestParam("rank") Integer rank
    ) {
        return null;
    }



//    // TODO TrackController에 포함 되어야 하지 않을까 한번더 확인 후 변경
//    @Operation(summary = "앨범의 트랙 등록" , description = "앨범의 트랙 등록")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "앨범의 트랙 등록 성공"),
//            @ApiResponse(responseCode = "400", description = "error")
//    })
//    @PostMapping("/{albumId}/tracks")
//    public void tracksInsert(@RequestBody TrackRequestDto dto) {
//
//    }
//
//    @Operation(summary = "앨범의 트랙 리스트 조회", description = "앨범의 트랙 리스트 조회")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "앨범의 트랙 리스트 조회 완료"),
//            @ApiResponse(responseCode = "400", description = "error")
//    })
//    @GetMapping("/{albumId}")
//    public TrackListReponseDto getTrackListDto(
//            @RequestParam("albumId") String albumId
//    ) {
//        return null;
//    }

    // header 에 jwt 존재하는 ID 가져와서 조회 가능.
    // 아티스트면 본인 앨범만 조회, 어드민이면 전체 조회.
    // 최신 등록순.
    @Operation(summary = "앨범 리스트 조회", description = "앨범 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "앨범 리스트 조회 완료"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @GetMapping
    public AlbumListReponseDto getAlbumListDto(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam("keyword") String keyword
    ) {
        return null;
    }
}
