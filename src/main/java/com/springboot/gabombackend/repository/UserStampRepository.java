package com.springboot.gabombackend.repository;

import com.springboot.gabombackend.dto.UserStampResponse;
import com.springboot.gabombackend.entity.UserStamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserStampRepository extends JpaRepository<UserStamp, Long> {

    @Query("SELECT new com.springboot.gabombackend.dto.UserStampResponse( " +
            "s.id, s.name, s.category, s.imageUrl, SUM(us.count)) " +
            "FROM UserStamp us " +
            "JOIN us.stamp s " +
            "WHERE us.user.id = :userId " +
            "GROUP BY s.id, s.name, s.category, s.imageUrl")
    List<UserStampResponse> findUserStampsWithSum(@Param("userId") Long userId);
}
