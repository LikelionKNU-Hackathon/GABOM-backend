package com.springboot.gabombackend.store;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    boolean existsByUserIdAndStoreIdAndVerified(Long userId, Long storeId, boolean verified);
}
