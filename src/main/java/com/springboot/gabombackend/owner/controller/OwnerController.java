package com.springboot.gabombackend.owner.controller;

import com.springboot.gabombackend.owner.entity.Owner;
import com.springboot.gabombackend.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    // 업주 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");
        String password = request.get("password");
        String email = request.get("email");
        Long storeId = Long.valueOf(request.get("storeId"));

        Owner owner = ownerService.signUp(loginId, password, email, storeId);

        return ResponseEntity.ok(Map.of(
                "message", "업주 회원가입이 완료되었습니다.",
                "ownerId", owner.getId(),
                "storeId", storeId
        ));
    }

    // 업주 로그인
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
}
