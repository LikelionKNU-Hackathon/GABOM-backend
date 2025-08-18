package com.springboot.gabombackend.ranking;

import com.springboot.gabombackend.ranking.dto.RankingDto;
import com.springboot.gabombackend.ranking.dto.RankingResponse;
import com.springboot.gabombackend.user.User;
import com.springboot.gabombackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final UserRepository userRepository;

    // loginId → userId 변환 후 랭킹 계산
    public RankingResponse getRankingByLoginId(String loginId, int page, int limit) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return getRanking(user.getId(), page, limit);
    }

    public RankingResponse getRanking(Long myUserId, int page, int limit) {
        // 1. 전체 랭킹 불러오기
        List<RankingDto> allRankings = rankingRepository.findAllRankings();

        // 2. 순위 매기기
        int rank = 1;
        for (RankingDto dto : allRankings) {
            dto.setRank(rank++);
        }

        // 3. Top 3
        List<RankingDto> topRanks = allRankings.size() >= 3
                ? allRankings.subList(0, 3)
                : new ArrayList<>(allRankings);

        // 4. 내 순위 (myUserId로 찾기)
        RankingDto myRank = allRankings.stream()
                .filter(r -> Objects.equals(r.getUserId(), myUserId))
                .findFirst()
                .orElse(null);

        // 5. 나머지 랭킹 (페이지네이션: 4위부터 시작)
        int start = 3 + (page - 1) * limit;
        int end = Math.min(start + limit, allRankings.size());
        List<RankingDto> otherRanks = start < end
                ? new ArrayList<>(allRankings.subList(start, end))
                : new ArrayList<>();

        // 6. 최종 응답
        return new RankingResponse(topRanks, myRank, otherRanks);
    }
}
