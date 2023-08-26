package com.github.bluekey.controller.member;

import com.github.bluekey.dto.response.admin.AdminAndArtistAccountResponseDto;
import com.github.bluekey.dto.response.artist.ArtistAccountsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "Member 관련 API")
@RestController
@RequestMapping("/api/v1/Members")
@RequiredArgsConstructor
public class MemberController {

}
