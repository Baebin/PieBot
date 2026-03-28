package com.piebin.piebot.yacht.domain;

import com.piebin.piebot.global.domain.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Yacht {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne
    private Account account;

    @Builder.Default
    private Long win = 0L;

    @Builder.Default
    private Long tie = 0L;

    @Builder.Default
    private Long lose = 0L;
}
