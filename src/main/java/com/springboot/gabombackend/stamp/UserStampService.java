package com.springboot.gabombackend.stamp;

import com.springboot.gabombackend.stamp.dto.UserStampResponse;
import com.springboot.gabombackend.title.UserTitleService;
import com.springboot.gabombackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserStampService {

    private final UserStampRepository userStampRepository;
    private final UserTitleService userTitleService;

    // 내 스탬프 조회
    public List<UserStampResponse> getUserStamps(Long userId) {
        return userStampRepository.findUserStampsWithSum(userId);
    }

    // 스탬프 적립
    public void addStamp(User user, Stamp stamp) {
        // 기존 유저 스탬프 가져오기
        UserStamp userStamp = userStampRepository.findByUserAndStamp(user, stamp)
                .orElse(new UserStamp(user, stamp, 0));

        // 스탬프 개수 증가
        userStamp.setCount(userStamp.getCount() + 1);
        userStampRepository.save(userStamp);

        // 칭호 진행도 업데이트 (자동 획득)
        userTitleService.updateUserTitleProgress(user, stamp.getCategory());
    }
}
