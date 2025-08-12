package com.springboot.gabombackend.service;

import com.springboot.gabombackend.dto.LoginRequest;
import com.springboot.gabombackend.dto.SignUpRequest;
import com.springboot.gabombackend.dto.mypagedto.UserUpdateDto;
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
    private final MailService mailService;

    public void findIdAndSendEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> mailService.sendIdReminder(email, user.getLoginId()));
        // 존재하지 않아도 그냥 아무것도 안 함
    }

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

    // 회원가입
    @Transactional
    public Long signUp(SignUpRequest req) {
        String encodedPw = passwordEncoder.encode(req.getPassword());
        User saved = userRepository.save(req.toEntity(encodedPw));
        return saved.getId();
    }

    // 로그인
    public User login(LoginRequest req) {
        return userRepository.findByLoginId(req.getLoginId())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPassword()))
                .orElse(null);
    }

    // loginId로 사용자 조회
    public User getByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElse(null);
    }

    // 마이페이지 정보 수정
    @Transactional
    public User updateUser(String loginId, UserUpdateDto dto) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getNickname() != null && !dto.getNickname().isBlank()) {
            user.setNickname(dto.getNickname());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String encodedPw = passwordEncoder.encode(dto.getPassword());
            user.setPassword(encodedPw);
        }

        return userRepository.save(user);
    }
}
