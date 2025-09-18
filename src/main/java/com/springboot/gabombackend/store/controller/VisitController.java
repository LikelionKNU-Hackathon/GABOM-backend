package com.springboot.gabombackend.store.controller;

import com.springboot.gabombackend.store.service.VisitService;
import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;
    private final UserRepository userRepository;

    // QR 인증 + 스탬프 적립
    @PostMapping("/verify")
    public ResponseEntity<?> verifyVisit(@RequestParam Long storeId, Principal principal) {
        String loginId = principal.getName();
        User currentUser = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        String message = visitService.verifyVisitAndAddStamp(currentUser.getId(), storeId);
        return ResponseEntity.ok().body(message);
    }
}
