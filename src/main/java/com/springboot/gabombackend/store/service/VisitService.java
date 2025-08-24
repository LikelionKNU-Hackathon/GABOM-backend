package com.springboot.gabombackend.store.service;

import com.springboot.gabombackend.stamp.entity.Stamp;
import com.springboot.gabombackend.stamp.entity.UserStamp;
import com.springboot.gabombackend.stamp.repository.StampRepository;
import com.springboot.gabombackend.stamp.repository.UserStampRepository;
import com.springboot.gabombackend.store.entity.Store;
import com.springboot.gabombackend.store.entity.Visit;
import com.springboot.gabombackend.store.repository.StoreRepository;
import com.springboot.gabombackend.store.repository.VisitRepository;
import com.springboot.gabombackend.title.service.UserTitleService;
import com.springboot.gabombackend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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

        // 카테고리별 스탬프 목록 가져오기
        List<Stamp> categoryStamps = stampRepository.findByCategory(store.getCategory());
        if (categoryStamps.isEmpty()) {
            throw new IllegalArgumentException("해당 카테고리에 등록된 스탬프가 없습니다: " + store.getCategory());
        }

        // 랜덤 스탬프 선택
        Stamp randomStamp = categoryStamps.get(new Random().nextInt(categoryStamps.size()));

        // 유저 스탬프 조회 or 신규 생성
        UserStamp userStamp = userStampRepository.findByUser_IdAndStamp_Id(userId, randomStamp.getId())
                .orElseGet(() -> userStampRepository.save(
                        UserStamp.builder()
                                .user(User.builder().id(userId).build())
                                .stamp(randomStamp)
                                .count(0)
                                .build()
                ));

        // 스탬프 증가
        userStamp.incrementCount();
        userStampRepository.save(userStamp);

        // 칭호 진행도 업데이트
        userTitleService.updateUserTitleProgress(User.builder().id(userId).build(), store.getCategory());

        // 결과 메시지 반환
        return String.format(
                "%s 방문 인증 완료! 🎉 '%s' 스탬프를 획득했습니다. 현재 %s 카테고리 스탬프 %d개 보유 중.",
                store.getName(),
                randomStamp.getName(),
                randomStamp.getCategory(),
                userStamp.getCount()
        );
    }
}
