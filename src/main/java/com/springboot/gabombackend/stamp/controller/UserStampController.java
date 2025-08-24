package com.springboot.gabombackend.stamp.controller;

import com.springboot.gabombackend.stamp.service.UserStampService;
import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserStampController {

    private final UserStampService userStampService;
    private final UserService userService;

    // 내 스탬프 보유 현황 조회
    @GetMapping("/stamps")
    public ResponseEntity<?> getMyStamps(Principal principal) {
        String loginId = principal.getName(); // JWT에서 loginId 추출
        User user = userService.getByLoginId(loginId);
        return ResponseEntity.ok(userStampService.getUserStamps(user.getId()));
    }
}
