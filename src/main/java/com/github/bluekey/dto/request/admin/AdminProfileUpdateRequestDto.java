package com.github.bluekey.dto.request.admin;

import com.github.bluekey.dto.admin.AdminProfileUpdateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "어드민 프로필 수정 요청")
public class AdminProfileUpdateRequestDto {
	@Schema(description = "프로필 이미지 파일")
	private MultipartFile file;
	@Schema(description = "어드민 프로필 수정 정보를 담은 dto")
	private AdminProfileUpdateDto data;

	public AdminProfileUpdateRequestDto(final MultipartFile file, final AdminProfileUpdateDto data) {
		this.file = file;
		this.data = data;
	}
}
