package com.piebin.piebot.model.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class YachtRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne
    private Account account;

    @OneToOne
    private Account opponent;

    @Column(name = "turn_count")
    @Builder.Default
    private Integer turnCount = 0;

    @Column(name = "roll_count")
    @Builder.Default
    private Integer rollCount = 0;

    @OneToOne(cascade = CascadeType.ALL)
    @Builder.Default
    private YachtScoreBoard accountScoreBoard = new YachtScoreBoard();

    @OneToOne(cascade = CascadeType.ALL)
    @Builder.Default
    private YachtScoreBoard opponentScoreBoard = new YachtScoreBoard();

    private String messageId;
}
