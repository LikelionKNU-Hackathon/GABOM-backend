package com.springboot.gabombackend.service;

import com.springboot.gabombackend.dto.TierProgress;
import com.springboot.gabombackend.entity.User;
import com.springboot.gabombackend.repository.UserStampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.springboot.gabombackend.dto.TierResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TierService {

    private final UserStampRepository userStampRepository;

    public TierResponse getUserTiers(User user) {
        // 1. 해당 유저의 총 스탬프 개수
        int totalStamps = userStampRepository.sumStampCountByUser(user.getId());

        // 2. 티어 기준표
        List<TierProgress> progressList = new ArrayList<>();
        progressList.add(new TierProgress("동네여행자", "🚶", totalStamps, 15, totalStamps >= 15));
        progressList.add(new TierProgress("골목마스터", "🚴", totalStamps, 30, totalStamps >= 30));
        progressList.add(new TierProgress("거리정복자", "🚗", totalStamps, 50, totalStamps >= 50));
        progressList.add(new TierProgress("지역탐험가", "✈️", totalStamps, 90, totalStamps >= 90));
        progressList.add(new TierProgress("전설의 가봄러", "🗺️", totalStamps, 200, totalStamps >= 200));

        // 3. 현재 티어 계산
        String currentTier = "초행자";
        for (int i = progressList.size() - 1; i >= 0; i--) {
            if (progressList.get(i).isAchieved()) {
                currentTier = progressList.get(i).getName();
                break;
            }
        }

        return new TierResponse(currentTier, progressList);
    }
}
