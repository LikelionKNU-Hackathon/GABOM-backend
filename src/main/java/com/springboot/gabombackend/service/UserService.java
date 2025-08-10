package com.springboot.gabombackend.service;

import com.springboot.gabombackend.dto.LoginRequest;
import com.springboot.gabombackend.dto.SignUpRequest;
import com.springboot.gabombackend.entity.User;
import com.springboot.gabombackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 아이디 중복 여부
    public boolean existsLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    // 이메일 중복 여부
    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // 닉네임 중복 여부
    public boolean existsNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 회원가입: DTO -> 암호화 -> 저장 -> 생성된 PK 반환
    @Transactional
    public Long signUp(SignUpRequest req) {
        String encodedPw = passwordEncoder.encode(req.getPassword());
        User saved = userRepository.save(req.toEntity(encodedPw));
        return saved.getId();
    }

    // 로그인: loginId로 조회 -> 비밀번호 검증 -> 성공 시 User 반환, 실패 시 null
    public User login(LoginRequest req) {
        return userRepository.findByLoginId(req.getLoginId())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPassword()))
                .orElse(null);
    }

    // loginId로 사용자 조회 (JWT 필터 등에서 사용)
    public User getByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElse(null);
    }
}
