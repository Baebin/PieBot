package com.piebin.piebot.model.domain;

import com.piebin.piebot.model.converter.IntegerListConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "is_holding_dice")
    @Builder.Default
    private Boolean isHoldingDice = true;

    @OneToOne(cascade = CascadeType.ALL)
    @Builder.Default
    private YachtScoreBoard accountScoreBoard = new YachtScoreBoard();

    @OneToOne(cascade = CascadeType.ALL)
    @Builder.Default
    private YachtScoreBoard opponentScoreBoard = new YachtScoreBoard();

    @Convert(converter = IntegerListConverter.class)
    @Builder.Default
    private List<Integer> selectedDices = new ArrayList<>();

    @Convert(converter = IntegerListConverter.class)
    @Builder.Default
    private List<Integer> nonSelectedDices = new ArrayList<>();

    private String channelId;
    private String messageId;

    public boolean canRoll() {
        return (rollCount < 3);
    }

    public void increaseRollCount() {
        rollCount++;
    }

    public List<Integer> getDices() {
        List<Integer> dices = new ArrayList<>();
        dices.addAll(selectedDices);
        dices.addAll(nonSelectedDices);
        return dices;
    }

    public void nextTurn() {
        turnCount += 1;
        rollCount = 0;
        isHoldingDice = true;
        selectedDices.clear();
        nonSelectedDices.clear();
    }
}
