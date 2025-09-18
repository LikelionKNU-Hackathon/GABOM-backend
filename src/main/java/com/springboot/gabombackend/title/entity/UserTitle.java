package com.springboot.gabombackend.title.entity;

import com.springboot.gabombackend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "user_titles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 유저의 칭호인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_title_user"))
    @OnDelete(action = OnDeleteAction.CASCADE) // 유저 삭제 시 자동 삭제
    private User user;

    // 어떤 칭호인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_title_title"))
    private Title title;

    // 현재 진행 횟수
    @Column(nullable = false)
    private int currentCount = 0;

    // 달성 여부
    @Column(nullable = false)
    private boolean achieved = false;

    // 대표 칭호 여부
    @Column(nullable = false)
    private boolean representative = false;

    public UserTitle(User user, Title title, int currentCount, boolean achieved, boolean representative) {
        this.user = user;
        this.title = title;
        this.currentCount = currentCount;
        this.achieved = achieved;
        this.representative = representative;
    }
}
