package com.springboot.gabombackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_stamps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 유저가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 어떤 스탬프를
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id", nullable = false)
    private Stamp stamp;

    // 몇 개 가졌는지 (중복 허용)
    @Column(nullable = false)
    private int count = 0;

    // 스탬프 추가 적립용 편의 메소드
    public void incrementCount() {
        this.count++;
    }

    public UserStamp(User user, Stamp stamp, int count) {
        this.user = user;
        this.stamp = stamp;
        this.count = count;
    }
}
