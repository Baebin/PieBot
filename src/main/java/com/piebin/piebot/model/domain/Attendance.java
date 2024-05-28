package com.piebin.piebot.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne
    private Account account;

    @Builder.Default
    private Long count = 0L;

    @Builder.Default
    private Long rewardCount = 0L;

    private LocalDateTime dateTime;
    private LocalDateTime rewardDateTime;
}
