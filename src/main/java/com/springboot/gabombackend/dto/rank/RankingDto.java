package com.springboot.gabombackend.dto.rank;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RankingDto {
    private Long userId;
    private int rank;
    private String nickname;
    private long stampCount;

    // JPQL에서 매핑할 생성자
    public RankingDto(Long userId, String nickname, long stampCount) {
        this.userId = userId;
        this.nickname = nickname;
        this.stampCount = stampCount;
        this.rank = 0;
    }
}