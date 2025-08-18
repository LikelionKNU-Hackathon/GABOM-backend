package com.springboot.gabombackend.store;

import com.springboot.gabombackend.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 방문 기록 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 방문한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;  // 방문한 가게

    @Column(name = "visited_at", nullable = false)
    private LocalDateTime visitedAt;  // 방문 시간

    @Column(name = "is_verified", nullable = false)
    private boolean verified;  // QR 인증 성공 여부
}
