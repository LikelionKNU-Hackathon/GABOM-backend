package com.springboot.gabombackend.owneranalysis.repository;

import com.springboot.gabombackend.owneranalysis.entity.CompetitionResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompetitionResultRepository extends JpaRepository<CompetitionResultEntity, Long> {
    // 특정 가게의 분석 결과 조회
    Optional<CompetitionResultEntity> findByStoreId(Long storeId);
}