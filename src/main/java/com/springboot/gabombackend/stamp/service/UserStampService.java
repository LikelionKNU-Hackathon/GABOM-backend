package com.springboot.gabombackend.stamp.service;

import com.springboot.gabombackend.stamp.entity.Stamp;
import com.springboot.gabombackend.stamp.entity.UserStamp;
import com.springboot.gabombackend.stamp.repository.StampRepository;
import com.springboot.gabombackend.stamp.repository.UserStampRepository;
import com.springboot.gabombackend.stamp.dto.UserStampResponse;
import com.springboot.gabombackend.title.service.UserTitleService;
import com.springboot.gabombackend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class UserStampService {

    private final UserStampRepository userStampRepository;
    private final StampRepository stampRepository;
    private final UserTitleService userTitleService;

    // 내 스탬프 조회
    public List<UserStampResponse> getUserStamps(Long userId) {
        return userStampRepository.findUserStampsWithSum(userId);
    }

    // 카테고리 기반 랜덤 스탬프 적립
    public UserStamp addStampByCategory(User user, String category) {
        List<Stamp> categoryStamps = stampRepository.findByCategory(category);
        if (categoryStamps.isEmpty()) {
            throw new IllegalArgumentException("해당 카테고리에 등록된 스탬프가 없습니다: " + category);
        }

        // 랜덤 선택
        Stamp randomStamp = categoryStamps.get(new Random().nextInt(categoryStamps.size()));

        // 기존 유저 스탬프 가져오기 (없으면 새로 생성)
        UserStamp userStamp = userStampRepository.findByUserAndStamp(user, randomStamp)
                .orElseGet(() -> userStampRepository.save(new UserStamp(user, randomStamp, 0)));

        // 스탬프 개수 증가
        userStamp.setCount(userStamp.getCount() + 1);
        userStampRepository.save(userStamp);

        // 칭호 진행도 업데이트
        userTitleService.updateUserTitleProgress(user, randomStamp.getCategory());

        return userStamp;
    }
}
