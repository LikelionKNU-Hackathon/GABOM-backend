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

    private final RewardRepository rewardRepository;
    private final UserRewardRepository userRewardRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserReward exchangeReward(Long userId, Long rewardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new IllegalArgumentException("리워드를 찾을 수 없습니다."));

        if (user.getAvailableStampCount() < reward.getStampNeeded()) {
            throw new IllegalArgumentException("스탬프가 부족합니다.");
        }

        user.setAvailableStampCount(user.getAvailableStampCount() - reward.getStampNeeded());
        userRepository.save(user);

        UserReward userReward = UserReward.builder()
                .user(user)
                .reward(reward)
                .barcode(generateBarcode()) // 바코드 랜덤 생성
                .build();

        userRewardRepository.save(userReward);

        return userReward;
    }

    public List<UserReward> getUserRewards(Long userId) {
        return userRewardRepository.findByUserId(userId);
    }

    private String generateBarcode() {
        return UUID.randomUUID().toString().substring(0, 12);
    }
}