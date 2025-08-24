package com.springboot.gabombackend.title.controller;

import com.springboot.gabombackend.title.service.UserTitleService;
import com.springboot.gabombackend.title.dto.UserTitleResponse;
import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me/titles")
public class TitleController {

    private final UserTitleService userTitleService;
    private final UserService userService;

    @GetMapping
    public List<UserTitleResponse> getMyTitles(Authentication authentication) {
        String loginId = authentication.getName();
        User user = userService.getByLoginId(loginId);
        return userTitleService.getMyTitles(user.getId());
    }

    @PatchMapping("/{titleId}")
    public String setRepresentativeTitle(Authentication authentication,
                                         @PathVariable Long titleId) {
        String loginId = authentication.getName();
        User user = userService.getByLoginId(loginId);
        userTitleService.setRepresentativeTitle(user.getId(), titleId);
        return "대표 칭호가 설정되었습니다.";
    }
}
