package com.springboot.gabombackend.controller;

import com.springboot.gabombackend.dto.CheckDuplicateResponse;
import com.springboot.gabombackend.dto.LoginRequest;
import com.springboot.gabombackend.dto.SignUpRequest;
import com.springboot.gabombackend.entity.User;
import com.springboot.gabombackend.jwt.JwtTokenUtil;
import com.springboot.gabombackend.repository.UserRepository;
import com.springboot.gabombackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${springboot.jwt.secret}")
    private String secretKey;


    // ✅ 중복 체크 API
    @GetMapping("/check")
    public CheckDuplicateResponse checkDuplicate(@RequestParam String type, @RequestParam String value) {
        boolean duplicate = switch (type) {
            case "loginId" -> userRepository.existsByLoginId(value);
            case "email" -> userRepository.existsByEmail(value);
            case "nickname" -> userRepository.existsByNickname(value);
            default -> throw new IllegalArgumentException("잘못된 type 값입니다.");
        };

        return CheckDuplicateResponse.builder()
                .type(type)
                .value(value)
                .duplicate(duplicate)
                .build();
    }

    // ✅ 회원가입 API
    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest req) {
        if (userService.existsLoginId(req.getLoginId())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "DuplicateIdException",
                    "message", "이미 존재하는 아이디입니다."
            ));
        }
        if (userService.existsEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "DuplicateEmailException",
                    "message", "이미 존재하는 이메일입니다."
            ));
        }
        if (req.getNickname() != null && !req.getNickname().isBlank()
                && userService.existsNickname(req.getNickname())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "DuplicateNicknameException",
                    "message", "이미 존재하는 닉네임입니다."
            ));
        }

        userService.signUp(req);
        return ResponseEntity.ok(Map.of(
                "message", "회원가입이 완료되었습니다.",
                "userId", req.getLoginId()
        ));
    }

    // ✅ 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = userService.login(req);

        if (user == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "InvalidCredentials",
                    "message", "아이디 또는 비밀번호가 올바르지 않습니다."
            ));
        }

        long expireTimeMs = 1000 * 60 * 60; // 60분
        String token = JwtTokenUtil.createToken(user.getLoginId(), secretKey, expireTimeMs);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getLoginId());
        userInfo.put("nickname", user.getNickname());

        return ResponseEntity.ok(Map.of(
                "accessToken", token,
                "user", userInfo
        ));
    }
}
