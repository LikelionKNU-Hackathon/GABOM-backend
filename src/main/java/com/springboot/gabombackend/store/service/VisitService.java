package com.springboot.gabombackend.store.service;

import com.springboot.gabombackend.stamp.entity.Stamp;
import com.springboot.gabombackend.stamp.repository.StampRepository;
import com.springboot.gabombackend.stamp.entity.UserStamp;
import com.springboot.gabombackend.stamp.repository.UserStampRepository;
import com.springboot.gabombackend.store.entity.Store;
import com.springboot.gabombackend.store.repository.StoreRepository;
import com.springboot.gabombackend.store.entity.Visit;
import com.springboot.gabombackend.store.repository.VisitRepository;
import com.springboot.gabombackend.title.service.UserTitleService;
import com.springboot.gabombackend.user.entity.User;
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
    private final StampRepository stampRepository;
    private final UserTitleService userTitleService;

    @Transactional
    public String verifyVisitAndAddStamp(Long userId, Long storeId) {
        // 가게 확인
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        // 방문 기록 저장
        Visit visit = Visit.builder()
                .user(User.builder().id(userId).build())
                .store(store)
                .visitedAt(LocalDateTime.now())
                .verified(true)
                .build();
        visitRepository.save(visit);

        // 스탬프 종류 찾기
        Stamp stamp = stampRepository.findFirstByCategory(store.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리에 맞는 스탬프가 없습니다."));

        // 유저 스탬프 조회 or 생성
        UserStamp userStamp = userStampRepository.findByUserIdAndStampId(userId, stamp.getId())
                .orElse(UserStamp.builder()
                        .user(User.builder().id(userId).build())
                        .stamp(stamp)
                        .count(0)
                        .build());

        // 스탬프 증가
        userStamp.incrementCount();
        userStampRepository.save(userStamp);

        // 칭호 진행도 업데이트
        userTitleService.updateUserTitleProgress(User.builder().id(userId).build(), store.getCategory());

        // 결과 메시지 반환
        return store.getName() + " 방문 인증 완료! 현재 " + stamp.getCategory() + " 스탬프 " + userStamp.getCount() + "개 보유 중.";
    }
}