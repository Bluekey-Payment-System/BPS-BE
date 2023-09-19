package com.github.bluekey.controller.member;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "Member 관련 API")
@RestController
@RequestMapping("/api/v1/Members")
@RequiredArgsConstructor
public class MemberController {

}
