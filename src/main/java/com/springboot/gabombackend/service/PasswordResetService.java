package com.springboot.gabombackend.service;

import com.springboot.gabombackend.entity.User;
import com.springboot.gabombackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private static class CodeInfo {
        String code;
        long expireTime;
    }

    // 발급된 코드 저장
    private final ConcurrentHashMap<String, CodeInfo> codeStorage = new ConcurrentHashMap<>();
    // 인증된 이메일 저장
    private final ConcurrentHashMap<String, Boolean> verifiedEmailMap = new ConcurrentHashMap<>();

    // 1단계: 코드 발송
    public void sendVerificationCode(String email) {
        boolean exists = true; // 가입 여부 확인 로직 필요
        if (!exists) {
            throw new IllegalArgumentException("해당 이메일로 가입된 계정이 없습니다.");
        }

        String code = generateCode();

        // 5분 유효기간 설정
        CodeInfo info = new CodeInfo();
        info.code = code;
        info.expireTime = System.currentTimeMillis() + (5 * 60 * 1000);

        codeStorage.put(email, info);

        sendEmail(email, "[GABOM] 비밀번호 재설정 인증 코드",
                "다음 인증 코드를 입력해주세요 (5분 유효): " + code);

        log.info("이메일 [{}]로 전송된 인증 코드: {} (5분 유효)", email, code);
    }

    public boolean verifyCode(String email, String code) {
        CodeInfo stored = codeStorage.get(email);

        if (stored == null) return false; // 코드 없음

        if (System.currentTimeMillis() > stored.expireTime) {
            codeStorage.remove(email); // 만료 시 삭제
            return false;
        }

        if (stored.code.equals(code)) {
            verifiedEmailMap.put(email, true); // 인증 상태 저장
            return true;
        }

        return false;
    }

    public boolean resetPassword(String newPassword) {
        String email = verifiedEmailMap.keySet().stream()
                .filter(verifiedEmailMap::get)
                .findFirst()
                .orElse(null);

        if (email == null) {
            return false;
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        clearVerification(email);
        return true;
    }

    // 이메일 발송
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    // 6자리 랜덤 코드 생성
    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int num = 100000 + random.nextInt(900000);
        return String.valueOf(num);
    }

    // 인증 여부 확인
    public boolean isVerified(String email) {
        return verifiedEmailMap.getOrDefault(email, false);
    }

    // 인증 및 코드 삭제 (보안성)
    public void clearVerification(String email) {
        verifiedEmailMap.remove(email);
        codeStorage.remove(email);
    }
}
