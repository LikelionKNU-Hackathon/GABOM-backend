package com.springboot.gabombackend.store;

import com.springboot.gabombackend.stamp.Stamp;
import com.springboot.gabombackend.stamp.StampRepository;
import com.springboot.gabombackend.stamp.UserStamp;
import com.springboot.gabombackend.stamp.UserStampRepository;
import com.springboot.gabombackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final StoreRepository storeRepository;
    private final VisitRepository visitRepository;
    private final UserStampRepository userStampRepository;
    private final StampRepository stampRepository; // Stamp 기본 데이터 관리용

    @Transactional
    public String verifyVisitAndAddStamp(Long userId, Long storeId) {
        // 1. 가게 존재 여부 확인
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        // 2. Visit 기록 저장 (QR 인증 성공)
        Visit visit = Visit.builder()
                .user(User.builder().id(userId).build())
                .store(store)
                .visitedAt(LocalDateTime.now())
                .verified(true)
                .build();
        visitRepository.save(visit);

        // 3. 스탬프 종류(가게 카테고리 기준) 찾기
        Stamp stamp = stampRepository.findFirstByCategory(store.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리에 맞는 스탬프가 없습니다."));

        // 4. 유저가 이미 같은 스탬프를 보유했는지 확인
        UserStamp userStamp = userStampRepository.findByUserIdAndStampId(userId, stamp.getId())
                .orElse(UserStamp.builder()
                        .user(User.builder().id(userId).build())
                        .stamp(stamp)
                        .count(0)
                        .build());

        // 5. 스탬프 개수 증가
        userStamp.incrementCount();
        userStampRepository.save(userStamp);

        return store.getName() + " 방문 인증 완료! 현재 " + stamp.getCategory() + " 스탬프 " + userStamp.getCount() + "개 보유 중.";
    }
}
