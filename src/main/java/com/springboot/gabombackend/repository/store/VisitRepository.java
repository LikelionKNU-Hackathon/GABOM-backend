package com.springboot.gabombackend.repository.store;

import com.springboot.gabombackend.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    boolean existsByUserIdAndStoreIdAndVerified(Long userId, Long storeId, boolean verified);
}
