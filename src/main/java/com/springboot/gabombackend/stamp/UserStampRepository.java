package com.springboot.gabombackend.stamp;

import com.springboot.gabombackend.stamp.dto.UserStampResponse;
import com.springboot.gabombackend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserStampRepository extends JpaRepository<UserStamp, Long> {

    // 특정 유저가 가진 스탬프 목록 + 합계 개수
    @Query("SELECT new com.springboot.gabombackend.stamp.dto.UserStampResponse( " +
            "s.id, s.name, s.category, s.imageUrl, SUM(us.count)) " +
            "FROM UserStamp us " +
            "JOIN us.stamp s " +
            "WHERE us.user.id = :userId " +
            "GROUP BY s.id, s.name, s.category, s.imageUrl")
    List<UserStampResponse> findUserStampsWithSum(@Param("userId") Long userId);

    // 유저와 스탬프로 단일 조회
    Optional<UserStamp> findByUserAndStamp(User user, Stamp stamp);

    // userId, stampId로 단일 조회
    Optional<UserStamp> findByUserIdAndStampId(Long userId, Long stampId);

    // 유저가 가진 모든 스탬프 총 개수
    @Query("SELECT COALESCE(SUM(us.count), 0) " +
            "FROM UserStamp us " +
            "WHERE us.user.id = :userId")
    int sumStampCountByUser(@Param("userId") Long userId);
}