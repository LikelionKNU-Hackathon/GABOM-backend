package com.springboot.gabombackend.user.entity;

import com.springboot.gabombackend.journal.entity.UserJournalCase;
import com.springboot.gabombackend.stamp.entity.UserStamp;
import com.springboot.gabombackend.store.entity.Visit;
import com.springboot.gabombackend.title.entity.UserTitle;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

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

    // 로그인 아이디
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

    // 권한 (USER, OWNER, ADMIN)
    @Column(nullable = false)
    private String role = "USER";

    // 유저가 가진 스탬프들
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStamp> userStamps = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserJournalCase journalCase;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTitle> userTitles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();

    @Column(nullable = false)
    private int availableStampCount = 0;

}
