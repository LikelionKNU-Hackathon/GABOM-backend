package com.springboot.gabombackend.owneranalysis.repository;

import com.springboot.gabombackend.owneranalysis.entity.SentimentResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SentimentResultRepository extends JpaRepository<SentimentResultEntity, Long> {
    // 특정 가게의 분석 결과 조회
    Optional<SentimentResultEntity> findByStoreId(Long storeId);
}
