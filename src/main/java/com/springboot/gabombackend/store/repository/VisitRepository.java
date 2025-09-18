package com.springboot.gabombackend.store.repository;

import com.springboot.gabombackend.store.entity.Store;
import com.springboot.gabombackend.store.entity.Visit;
import com.springboot.gabombackend.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    boolean existsByUserIdAndStoreIdAndVerified(Long userId, Long storeId, boolean verified);

    // 특정 유저가 특정 가게 방문한 횟수
    @Query("SELECT COUNT(v) FROM Visit v WHERE v.user = :user AND v.store = :store")
    int findMyVisitCount(@Param("user") User user, @Param("store") Store store);

    // 특정 가게에서 방문 횟수 상위 유저 (최대 방문자)
    @Query("SELECT v.user, COUNT(v) as cnt " +
            "FROM Visit v " +
            "WHERE v.store = :store " +
            "GROUP BY v.user " +
            "ORDER BY cnt DESC")
    List<Object[]> findTopVisitors(@Param("store") Store store, Pageable pageable);
}
