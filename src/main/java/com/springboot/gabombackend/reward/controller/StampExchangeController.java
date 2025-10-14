package com.springboot.gabombackend.reward.controller;

import com.springboot.gabombackend.reward.entity.UserReward;
import com.springboot.gabombackend.reward.service.StampExchangeService;
import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StampExchangeController {

    private final StampExchangeService stampExchangeService;
    private final UserService userService;

    @GetMapping("/store")
    public ResponseEntity<Map<String, Object>> getStorePage() {
        // 토큰에서 loginId 추출
        String loginId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // 유저 정보 조회
        User user = userService.getByLoginId(loginId);

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("availableStampCount", user.getAvailableStampCount());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/me/rewards")
    public ResponseEntity<List<UserReward>> getMyRewards() {
        String loginId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userService.getByLoginId(loginId);

        List<UserReward> rewards = stampExchangeService.getUserRewards(user.getId());
        return ResponseEntity.ok(rewards);
    }

    @PostMapping("/stamps/exchange")
    public ResponseEntity<UserReward> exchangeReward(@RequestParam Long rewardId) {
        String loginId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userService.getByLoginId(loginId);

        UserReward result = stampExchangeService.exchangeReward(user.getId(), rewardId);
        return ResponseEntity.ok(result);
    }
}
