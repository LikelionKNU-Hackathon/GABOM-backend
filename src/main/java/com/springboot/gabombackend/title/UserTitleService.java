package com.springboot.gabombackend.title;

import com.springboot.gabombackend.title.dto.UserTitleResponse;
import com.springboot.gabombackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTitleService {

    private final TitleRepository titleRepository;
    private final UserTitleRepository userTitleRepository;

    // 칭호 진행도 업데이트 (스탬프 적립 시 호출)
    public void updateUserTitleProgress(User user, String category) {
        Title title = titleRepository.findByCategory(category)
                .orElseThrow(() -> new RuntimeException("칭호 정보 없음"));

        UserTitle userTitle = userTitleRepository.findByUserAndTitleId(user, title.getId())
                .orElse(new UserTitle(user, title, 0, false, false));

        userTitle.setCurrentCount(userTitle.getCurrentCount() + 1);
        if (userTitle.getCurrentCount() >= title.getGoalCount() && !userTitle.isAchieved()) {
            userTitle.setAchieved(true);
        }
        userTitleRepository.save(userTitle);
    }

    // 내 칭호 목록 조회
    public List<UserTitleResponse> getMyTitles(User user) {
        return userTitleRepository.findByUser(user).stream()
                .map(ut -> new UserTitleResponse(
                        ut.getTitle().getId(),
                        ut.getTitle().getName(),
                        ut.getTitle().getDescription(),
                        ut.getCurrentCount(),
                        ut.getTitle().getGoalCount(),
                        ut.isAchieved(),
                        ut.isRepresentative()
                ))
                .collect(Collectors.toList());
    }

    // 대표 칭호 설정
    public void setRepresentativeTitle(User user, Long titleId) {
        UserTitle userTitle = userTitleRepository.findByUserAndTitleId(user, titleId)
                .orElseThrow(() -> new RuntimeException("칭호를 보유하지 않음"));

        // 기존 대표 칭호 해제
        List<UserTitle> currentReps = userTitleRepository.findByUserAndRepresentativeTrue(user);
        currentReps.forEach(rep -> {
            rep.setRepresentative(false);
            userTitleRepository.save(rep);
        });

        // 새 대표 칭호 지정
        userTitle.setRepresentative(true);
        userTitleRepository.save(userTitle);
    }
}
