package com.piebin.piebot.model.domain;

import com.piebin.piebot.model.entity.OmokState;
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
}
