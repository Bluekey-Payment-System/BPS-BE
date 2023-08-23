package com.github.bluekey.dto.request.album;

import com.github.bluekey.dto.album.NewAlbumInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "새로 등록할 앨범 정보 (파일 / json 객체)")
public class AlbumsRequestDto {
    @Schema(description = "앨범 커버 이미지 파일")
    private MultipartFile file;
    @Schema(description = "앨범 정보")
    private NewAlbumInfoDto data;

    @Builder
    public AlbumsRequestDto(final MultipartFile file, final NewAlbumInfoDto data) {
        this.file = file;
        this.data = data;
    }
}
