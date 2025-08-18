package com.springboot.gabombackend.journal;

import com.springboot.gabombackend.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_journal_case")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJournalCase {

    @Id
    private Long userId; // 유저 ID = PK

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "case_id", nullable = false)
    private JournalCase journalCase;
}
