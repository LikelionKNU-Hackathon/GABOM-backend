package com.springboot.gabombackend.store.controller;

import com.springboot.gabombackend.store.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;

    // QR 인증 + 스탬프 적립
    @PostMapping("/verify")
    public ResponseEntity<?> verifyVisit(@RequestParam Long storeId, Principal principal) {
        Long userId = Long.valueOf(principal.getName()); // JWT에서 유저 ID 꺼내오기
        String message = visitService.verifyVisitAndAddStamp(userId, storeId);
        return ResponseEntity.ok().body(message);
    }
}
