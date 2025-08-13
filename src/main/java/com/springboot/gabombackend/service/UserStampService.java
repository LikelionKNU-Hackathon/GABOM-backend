package com.springboot.gabombackend.service;

import com.springboot.gabombackend.dto.UserStampResponse;
import com.springboot.gabombackend.entity.Stamp;
import com.springboot.gabombackend.entity.User;
import com.springboot.gabombackend.entity.UserStamp;
import com.springboot.gabombackend.repository.UserStampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStampService {

    private final UserStampRepository userStampRepository;
    private final UserTitleService userTitleService;

    public List<UserStampResponse> getUserStamps(Long userId) {
        return userStampRepository.findUserStampsWithSum(userId);
    }
    public void addStamp(User user, Stamp stamp) {
        // 1. 기존 유저 스탬프 가져오기
        UserStamp userStamp = userStampRepository.findByUserAndStamp(user, stamp)
                .orElse(new UserStamp(user, stamp, 0));

        // 2. 스탬프 개수 증가
        userStamp.setCount(userStamp.getCount() + 1);
        userStampRepository.save(userStamp);

        // 3. 칭호 진행도 업데이트
        userTitleService.updateUserTitleProgress(user, stamp.getCategory());
    }
}
