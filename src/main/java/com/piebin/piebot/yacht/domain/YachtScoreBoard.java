package com.piebin.piebot.yacht.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YachtScoreBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    // 1 ~ 6
    private Integer aces;
    private Integer deuces;
    private Integer threes;
    private Integer fours;
    private Integer fives;
    private Integer sixes;

    private Integer bonus;

    private Integer choice;

    @Column(name = "four_of_a_kind")
    private Integer fourOfAKind;

    @Column(name = "full_house")
    private Integer fullHouse;

    @Column(name = "small_straight")
    private Integer smallStraight;

    @Column(name = "large_straight")
    private Integer largeStraight;

    @Column(name = "yacht")
    private Integer yacht;

    public List<Integer> getNumberScores() {
        return Arrays.asList(
                aces, deuces, threes,
                fours, fives, sixes
        );
    }

    public int getTotalScores() {
        int numberScores = (aces != null ? aces : 0) + (deuces != null ? deuces : 0) + (threes != null ? threes : 0)
                + (fours != null ? fours : 0) + (fives != null ? fives : 0) + (sixes != null ? sixes : 0);
        return (
                numberScores
                        + (bonus != null ? bonus : 0)
                        + (choice != null ? choice : 0)
                        + (fourOfAKind != null ? fourOfAKind : 0)
                        + (fullHouse != null ? fullHouse : 0)
                        + (smallStraight != null ? smallStraight : 0)
                        + (largeStraight != null ? largeStraight : 0)
                        + (yacht != null ? yacht : 0)
        );
    }
}
