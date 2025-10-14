package com.springboot.gabombackend.reward.service;

import com.springboot.gabombackend.reward.entity.Reward;
import com.springboot.gabombackend.reward.entity.UserReward;
import com.springboot.gabombackend.reward.repository.RewardRepository;
import com.springboot.gabombackend.reward.repository.UserRewardRepository;
import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StampExchangeService {

    private final UserRepository userRepository;
    private final RewardRepository rewardRepository;
    private final UserRewardRepository userRewardRepository;

    // 스탬프 교환 로직
    @Transactional
    public UserReward exchangeReward(Long userId, Long rewardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new IllegalArgumentException("리워드를 찾을 수 없습니다."));

        if (user.getAvailableStampCount() < reward.getStampNeeded()) {
            throw new IllegalArgumentException("스탬프가 부족합니다.");
        }

        // 스탬프 차감
        user.setAvailableStampCount(user.getAvailableStampCount() - reward.getStampNeeded());

        // 랜덤 바코드 생성
        String barcode = UUID.randomUUID().toString().substring(0, 12);

        UserReward userReward = UserReward.builder()
                .user(user)
                .reward(reward)
                .barcode(barcode)
                .build();

        return userRewardRepository.save(userReward);
    }

    // 유저 교환 내역 조회
    public List<UserReward> getUserRewards(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return userRewardRepository.findByUser(user);
    }

    // 전체 리워드 목록 조회
    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }

    // 유저 스탬프 정보 조회
    public User getUserStampInfo(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }
}