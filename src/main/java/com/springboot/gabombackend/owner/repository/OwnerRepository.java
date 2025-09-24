package com.springboot.gabombackend.owner.repository;

import com.springboot.gabombackend.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByLoginId(String loginId);
    boolean existsByEmail(String email);
}
