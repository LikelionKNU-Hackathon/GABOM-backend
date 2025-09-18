package com.springboot.gabombackend.title.service;

import com.springboot.gabombackend.stamp.repository.UserStampRepository;
import com.springboot.gabombackend.title.entity.Title;
import com.springboot.gabombackend.title.repository.TitleRepository;
import com.springboot.gabombackend.title.entity.UserTitle;
import com.springboot.gabombackend.title.repository.UserTitleRepository;
import com.springboot.gabombackend.title.dto.UserTitleResponse;
import com.springboot.gabombackend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserTitleService {

    private final TitleRepository titleRepository;
    private final UserTitleRepository userTitleRepository;
    private final UserStampRepository userStampRepository;

    // 카테고리별 스탬프 적립 시 칭호 진행도 갱신
    public void updateUserTitleProgress(User user, String category) {
        Title title = titleRepository.findByCategory(category)
                .orElseThrow(() -> new RuntimeException("칭호 정보 없음"));

        // 카테고리별 스탬프 총합 가져오기
        int totalCount = userStampRepository.sumByUserAndCategory(user.getId(), category);

        // 유저 칭호 조회 (없으면 새로 생성)
        UserTitle userTitle = userTitleRepository.findByUserIdAndTitleId(user.getId(), title.getId())
                .orElseGet(() -> userTitleRepository.save(
                        new UserTitle(user, title, 0, false, false)
                ));

        // 진행도는 goalCount 이상 올라가지 않도록 cap 처리
        int displayCount = Math.min(totalCount, title.getGoalCount());
        userTitle.setCurrentCount(displayCount);

        // 목표치 달성 여부 체크
        if (totalCount >= title.getGoalCount()) {
            userTitle.setAchieved(true);
        }

        userTitleRepository.save(userTitle);
    }

    // 내 칭호 목록 조회 (획득 안한 칭호도 포함)
    public List<UserTitleResponse> getMyTitles(Long userId) {
        // 전체 칭호 가져오기
        List<Title> allTitles = titleRepository.findAll();

        return allTitles.stream().map(title -> {
            // 유저의 칭호 진행도 조회 (없으면 null)
            UserTitle ut = userTitleRepository.findByUserIdAndTitleId(userId, title.getId())
                    .orElse(null);

            int current = (ut != null) ? ut.getCurrentCount() : 0;
            boolean achieved = (ut != null) && ut.isAchieved();
            boolean representative = (ut != null) && ut.isRepresentative();

            return new UserTitleResponse(
                    title.getId(),
                    title.getName(),
                    title.getDescription(),
                    current,
                    title.getGoalCount(),
                    achieved,
                    representative
            );
        }).collect(Collectors.toList());
    }

    // 대표 칭호 설정
    public void setRepresentativeTitle(Long userId, Long titleId) {
        UserTitle userTitle = userTitleRepository.findByUserIdAndTitleId(userId, titleId)
                .orElseThrow(() -> new RuntimeException("칭호를 보유하지 않음"));

        // 기존 대표 칭호 해제
        List<UserTitle> currentReps = userTitleRepository.findByUserAndRepresentativeTrue(userTitle.getUser());
        currentReps.forEach(rep -> {
            rep.setRepresentative(false);
            userTitleRepository.save(rep);
        });

        // 새 대표 칭호 지정
        userTitle.setRepresentative(true);
        userTitleRepository.save(userTitle);
    }

}
