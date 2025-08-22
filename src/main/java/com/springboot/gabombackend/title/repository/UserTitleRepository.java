package com.springboot.gabombackend.title.repository;

import com.springboot.gabombackend.title.entity.UserTitle;
import com.springboot.gabombackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTitleRepository extends JpaRepository<UserTitle, Long> {
    List<UserTitle> findByUser(User user);
    Optional<UserTitle> findByUserIdAndTitleId(Long userId, Long titleId);
    List<UserTitle> findByUserAndRepresentativeTrue(User user);
    List<UserTitle> findByUserId(Long userId);
}
