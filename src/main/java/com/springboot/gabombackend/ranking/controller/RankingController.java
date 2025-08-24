package com.springboot.gabombackend.ranking.controller;

import com.springboot.gabombackend.ranking.service.RankingService;
import com.springboot.gabombackend.ranking.dto.RankingResponse;
import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;
    private final UserService userService;

    @GetMapping("/rankings")
    public ResponseEntity<RankingResponse> getRankings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication
    ) {
        String loginId = authentication.getName();
        User user = userService.getByLoginId(loginId);
        RankingResponse response = rankingService.getRanking(user.getId(), page, limit);
        return ResponseEntity.ok(response);
    }
}
