package com.springboot.gabombackend.user.controller;

import com.springboot.gabombackend.common.CheckDuplicateResponse;
import com.springboot.gabombackend.user.service.PasswordResetService;
import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.repository.UserRepository;
import com.springboot.gabombackend.user.service.UserService;
import com.springboot.gabombackend.user.dto.LoginRequest;
import com.springboot.gabombackend.user.dto.PasswordResetDtos;
import com.springboot.gabombackend.user.dto.SignUpRequest;
import com.springboot.gabombackend.auth.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordResetService passwordResetService;

    @Value("${springboot.jwt.secret}")
    private String secretKey;

    // 중복 체크 API
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

    // 회원가입 API
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

    // 로그인 API
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
        String token = JwtTokenUtil.createToken(
                user.getLoginId(),
                "USER",
                secretKey,
                expireTimeMs
        );

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getLoginId());
        userInfo.put("nickname", user.getNickname());

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(Map.of(
                        "accessToken", token,
                        "user", userInfo
                ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        userService.findIdAndSendEmail(email);

        return ResponseEntity.ok(Map.of(
                "message", "입력한 이메일로 아이디 안내 메일을 발송했습니다."
        ));
    }

    @PostMapping("/find-password")
    public ResponseEntity<?> sendResetCode(@RequestBody PasswordResetDtos.EmailRequest request) {
        passwordResetService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok("코드가 전송되었습니다.");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody PasswordResetDtos.VerifyCodeRequest request) {
        boolean success = passwordResetService.verifyCode(request.getEmail(), request.getCode());

        if (success) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.badRequest().body("코드 불일치");
        }
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDtos.ResetPasswordRequest request) {
        boolean success = passwordResetService.resetPassword(request.getNewPassword());
        if (success) {
            return ResponseEntity.ok("비밀번호 변경 완료");
        } else {
            return ResponseEntity.badRequest().body("이메일 인증이 완료되지 않았습니다.");
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe() {
        String loginId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = userService.getByLoginId(loginId);
        userService.deleteUser(user.getId());

        return ResponseEntity.ok(Map.of("message", "계정이 삭제되었습니다."));
    }
}
