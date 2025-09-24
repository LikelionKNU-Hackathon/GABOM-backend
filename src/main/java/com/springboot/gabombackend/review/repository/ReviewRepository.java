package com.springboot.gabombackend.review.repository;

import com.springboot.gabombackend.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 기존 페이징 조회 (리뷰 목록용)
    Page<Review> findByStoreId(Long storeId, Pageable pageable);

    // 내 가게 리뷰 전체 텍스트
    @Query("select r.content from Review r where r.store.id = :storeId")
    List<String> findContentsByStoreId(@Param("storeId") Long storeId);

    // 경쟁 가게 리뷰 전체 텍스트 (같은 카테고리 내, 내 가게 제외)
    @Query("select r.content from Review r " +
            "where r.store.category = :category " +
            "and r.store.id <> :storeId")
    List<String> findContentsByCategoryCompetitors(@Param("category") String category,
                                                   @Param("storeId") Long storeId);

    // 리뷰 개수 (긍/부정 분석 시 필요)
    int countByStoreId(Long storeId);
}