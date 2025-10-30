package com.springboot.gabombackend.owner.controller;

import com.springboot.gabombackend.owner.dto.OwnerResponse;
import com.springboot.gabombackend.owner.entity.Owner;
import com.springboot.gabombackend.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");
        String password = request.get("password");
        String email = request.get("email");

        // 사업자등록 관련 정보
        String businessNumber = request.get("businessNumber");
        String representativeName = request.get("representativeName");
        String openDate = request.get("openDate");

        Owner owner = ownerService.signUpWithVerification(
                loginId, password, email,
                businessNumber, representativeName, openDate
        );

        return ResponseEntity.ok(Map.of(
                "message", "사업자 인증 및 회원가입이 완료되었습니다.",
                "verified", owner.isVerified(),
                "ownerId", owner.getId(),
                "storeId", owner.getStore().getId(),
                "storeName", owner.getStore().getName()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");
        String password = request.get("password");

        String token = ownerService.login(loginId, password);

        return ResponseEntity.ok(Map.of(
                "accessToken", token,
                "role", "OWNER"
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<OwnerResponse> getMyInfo(Authentication authentication) {
        String loginId = authentication.getName();
        OwnerResponse response = ownerService.getMyInfo(loginId);
        return ResponseEntity.ok(response);
    }
}