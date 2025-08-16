package com.springboot.gabombackend.controller;

import com.springboot.gabombackend.dto.rank.RankingResponse;
import com.springboot.gabombackend.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/rankings")
    public ResponseEntity<RankingResponse> getRankings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal Long userId
    ) {
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        RankingResponse response = rankingService.getRanking(userId, page, limit);
        return ResponseEntity.ok(response);
    }
}
