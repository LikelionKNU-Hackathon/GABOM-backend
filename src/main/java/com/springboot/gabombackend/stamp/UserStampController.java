package com.springboot.gabombackend.stamp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserStampController {

    private final UserStampService userStampService;

    // 내 스탬프 보유 현황 조회
    @GetMapping("/stamps")
    public ResponseEntity<?> getMyStamps(Principal principal) {
        Long userId = Long.valueOf(principal.getName()); // JWT에서 유저 ID 가져오기
        return ResponseEntity.ok(userStampService.getUserStamps(userId));
    }
}
