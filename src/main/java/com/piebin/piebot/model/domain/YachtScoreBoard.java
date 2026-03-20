package com.piebin.piebot.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> numbers = new ArrayList<>();

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
}
