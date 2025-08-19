package com.springboot.gabombackend.store;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByName(String name);
    Optional<Store> findByAddress(String address);

    // 가게명, 카테고리, 주소에서 검색
    @Query("SELECT s FROM Store s " +
            "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(s.category) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(s.address) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Store> searchByKeyword(@Param("keyword") String keyword);
}