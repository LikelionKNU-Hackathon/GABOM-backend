package com.springboot.gabombackend.title.controller;

import com.springboot.gabombackend.title.service.UserTitleService;
import com.springboot.gabombackend.title.dto.UserTitleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me/titles")
public class TitleController {

    private final UserTitleService userTitleService;

    @GetMapping
    public List<UserTitleResponse> getMyTitles(@AuthenticationPrincipal Long userId) {
        return userTitleService.getMyTitles(userId);
    }

    @PatchMapping("/{titleId}")
    public String setRepresentativeTitle(@AuthenticationPrincipal Long userId,
                                         @PathVariable Long titleId) {
        userTitleService.setRepresentativeTitle(userId, titleId);
        return "대표 칭호가 설정되었습니다.";
    }
}
