package com.springboot.gabombackend.owner.service;

import com.springboot.gabombackend.auth.JwtTokenUtil;
import com.springboot.gabombackend.owner.dto.OwnerResponse;
import com.springboot.gabombackend.owner.entity.Owner;
import com.springboot.gabombackend.owner.repository.OwnerRepository;
import com.springboot.gabombackend.store.entity.Store;
import com.springboot.gabombackend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final StoreRepository storeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${springboot.jwt.secret}")
    private String secretKey;

    // 업주 회원가입
    public Owner signUp(String loginId, String password, String email, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        Owner owner = Owner.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .email(email)
                .store(store)
                .build();

        return ownerRepository.save(owner);
    }

    // 업주 로그인 (storeId 포함 토큰 발급)
    public String login(String loginId, String password) {
        Owner owner = ownerRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 업주 계정입니다."));

        if (!passwordEncoder.matches(password, owner.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        long expireTimeMs = 1000 * 60 * 60; // 60분

        // 업주 전용 토큰 생성 (loginId + role + storeId)
        return JwtTokenUtil.createTokenWithRoleAndStore(
                owner.getLoginId(),
                "OWNER",
                owner.getStore().getId(),
                secretKey,
                expireTimeMs
        );
    }

    // 업주 마이페이지 조회
    public OwnerResponse getMyInfo(String loginId) {
        Owner owner = ownerRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 업주 계정입니다."));

        return new OwnerResponse(
                owner.getLoginId(),
                owner.getStore().getName(),
                owner.getStore().getId()
        );
    }
}
