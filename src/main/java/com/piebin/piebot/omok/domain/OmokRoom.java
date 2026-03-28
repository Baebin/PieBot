package com.piebin.piebot.omok.domain;

import com.piebin.piebot.global.domain.Account;
import com.piebin.piebot.omok.entity.OmokSkin;
import com.piebin.piebot.omok.entity.OmokState;
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
public class OmokRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne
    private Account account;

    @OneToOne
    private Account opponent;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private OmokState state = OmokState.BLACK;

    private String messageId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "omok_skin")
    private OmokSkin omokSkin;
}
