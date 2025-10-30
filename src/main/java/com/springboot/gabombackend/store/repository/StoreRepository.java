package com.springboot.gabombackend.store.repository;

import com.springboot.gabombackend.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByName(String name);
    Optional<Store> findByAddress(String address);

    // 사업자등록번호로 매장 찾기
    Optional<Store> findByBusinessNumber(String businessNumber);

    // 사업자등록번호 존재 여부 확인
    boolean existsByBusinessNumber(String businessNumber);

    // 가게명, 카테고리, 주소에서 검색
    @Query("SELECT s FROM Store s " +
            "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(s.category) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(s.address) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Store> searchByKeyword(@Param("keyword") String keyword);

    // 전체 매장에서 랜덤 1개
    @Query(value = "SELECT * FROM stores ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Store> findRandom();

    // 특정 카테고리에서 랜덤 1개
    @Query(value = "SELECT * FROM stores WHERE category = :category ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Store> findRandomByCategory(@Param("category") String category);
}
