package com.springboot.gabombackend.title;

import com.springboot.gabombackend.title.dto.UserTitleResponse;
import com.springboot.gabombackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me/titles")
public class TitleController {

    private final UserTitleService userTitleService;

    // 칭호 목록 조회
    @GetMapping
    public List<UserTitleResponse> getMyTitles(@AuthenticationPrincipal User user) {
        return userTitleService.getMyTitles(user);
    }

    // 대표 칭호 설정
    @PatchMapping("/{titleId}")
    public String setRepresentativeTitle(@AuthenticationPrincipal User user,
                                         @PathVariable Long titleId) {
        userTitleService.setRepresentativeTitle(user, titleId);
        return "대표 칭호가 설정되었습니다.";
    }
}
