package com.springboot.gabombackend.title;

import com.springboot.gabombackend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTitleRepository extends JpaRepository<UserTitle, Long> {
    List<UserTitle> findByUser(User user);
    Optional<UserTitle> findByUserAndTitleId(User user, Long titleId);
    List<UserTitle> findByUserAndRepresentativeTrue(User user);
}
