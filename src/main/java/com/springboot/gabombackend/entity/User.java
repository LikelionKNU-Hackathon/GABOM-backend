package com.springboot.gabombackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이름
    @Column(nullable = false)
    private String username;

    //로그인 아이디
    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    // 이메일
    @Column(nullable = false, unique = true)
    private String email;

    // 닉네임
    @Column(nullable = false, unique = true)
    private String nickname;

    // 비밀번호
    @Column(nullable = false)
    private String password;
}