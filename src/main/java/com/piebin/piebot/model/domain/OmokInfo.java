package com.piebin.piebot.model.domain;

import com.piebin.piebot.model.entity.OmokState;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OmokInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    private OmokRoom room;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private OmokState state = OmokState.BLACK;

    @NonNull
    private String position;

    @CreatedDate
    @Column(name = "reg_date")
    private LocalDateTime regDate;
}
