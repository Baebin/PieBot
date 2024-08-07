package com.piebin.piebot.model.domain;

import com.piebin.piebot.model.entity.OmokSkin;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Omok {
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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "omok_skin")
    private OmokSkin omokSkin;
}
