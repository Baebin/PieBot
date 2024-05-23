package com.piebin.piebot.model.domain;

import com.piebin.piebot.model.entity.CommandSentence;
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
public class EasterEgg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne
    private Account account;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private CommandSentence sentence;

    @CreatedDate
    @Column(name = "reg_date")
    private LocalDateTime regDate;
}
