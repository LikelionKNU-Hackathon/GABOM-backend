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

    // 어떤 유저의 스탬프인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 어떤 스탬프 종류인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id", nullable = false)
    private Stamp stamp;

    // 스탬프 개수 (같은 스탬프를 여러 번 가질 수 있으므로)
    @Column(nullable = false)
    private int count;
}
