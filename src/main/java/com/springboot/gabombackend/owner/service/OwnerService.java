package com.springboot.gabombackend.owner.service;

import com.springboot.gabombackend.auth.JwtTokenUtil;
import com.springboot.gabombackend.owner.dto.OwnerResponse;
import com.springboot.gabombackend.owner.entity.Owner;
import com.springboot.gabombackend.owner.repository.OwnerRepository;
import com.springboot.gabombackend.store.entity.Store;
import com.springboot.gabombackend.store.repository.StoreRepository;
import com.springboot.gabombackend.storeVerify.external.NationalTaxApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final StoreRepository storeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final NationalTaxApiClient nationalTaxApiClient;

    @Value("${springboot.jwt.secret}")
    private String secretKey;

    public Owner signUpWithVerification(String loginId, String password, String email,
                                        String businessNumber, String representativeName, String openDate) {

        // 국세청 사업자 진위확인
        Map<String, String> result = nationalTaxApiClient.verify(businessNumber, representativeName, openDate);
        String valid = result.get("valid");
        String msg = result.get("validMsg");

        if (!"01".equals(valid)) { // 01 = 정상 일치
            log.warn("국세청 인증 실패: {}", msg);
            throw new IllegalArgumentException("사업자등록정보가 국세청 정보와 일치하지 않습니다. 사유: " + msg);
        }

        // 우리 서비스 DB 내 매장 확인
        Store store = storeRepository.findByBusinessNumber(businessNumber)
                .orElseThrow(() -> new IllegalArgumentException("우리 서비스에 등록되지 않은 사업자등록번호입니다."));

        // Owner 저장
        Owner owner = Owner.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .email(email)
                .store(store)
                .businessNumber(businessNumber)
                .representativeName(representativeName)
                .openDate(openDate)
                .verified(true)
                .build();

        return ownerRepository.save(owner);
    }

    // 로그인
    public String login(String loginId, String password) {
        Owner owner = ownerRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 업주 계정입니다."));

        if (!passwordEncoder.matches(password, owner.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        long expireTimeMs = 1000 * 60 * 60; // 60분

        return JwtTokenUtil.createTokenWithRoleAndStore(
                owner.getLoginId(),
                "OWNER",
                owner.getStore().getId(),
                secretKey,
                expireTimeMs
        );
    }

    // 마이페이지 조회
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