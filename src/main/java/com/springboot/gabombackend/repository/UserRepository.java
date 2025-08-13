package com.springboot.gabombackend.repository;

import com.springboot.gabombackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    Optional<User> findByLoginId(String loginId);
    Optional<User> findByEmail(String email);
}
