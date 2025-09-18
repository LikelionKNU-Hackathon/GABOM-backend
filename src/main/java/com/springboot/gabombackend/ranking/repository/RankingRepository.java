package com.springboot.gabombackend.ranking.repository;

import com.springboot.gabombackend.ranking.dto.RankingDto;
import com.springboot.gabombackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<User, Long> {

    // 전체 랭킹 (DTO로 바로 매핑)
    @Query("SELECT new com.springboot.gabombackend.ranking.dto.RankingDto(" +
            "u.id, u.nickname, COALESCE(SUM(us.count), 0)) " +
            "FROM User u LEFT JOIN UserStamp us ON u = us.user " +
            "GROUP BY u.id, u.nickname " +
            "ORDER BY SUM(us.count) DESC")
    List<RankingDto> findAllRankings();

    // 특정 유저의 순위만 뽑기 (myRank)
    @Query("SELECT new com.springboot.gabombackend.ranking.dto.RankingDto(" +
            "u.id, u.nickname, COALESCE(SUM(us.count), 0)) " +
            "FROM User u LEFT JOIN UserStamp us ON u = us.user " +
            "WHERE u.id = :userId " +
            "GROUP BY u.id, u.nickname")
    RankingDto findMyRanking(@Param("userId") Long userId);
}
