package com.springboot.gabombackend.dto.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RankingResponse {
    private List<RankingDto> topRanks;   // 상위 3명
    private RankingDto myRank;           // 내 순위
    private List<RankingDto> otherRanks; // 그 외 순위 (페이지네이션)
}