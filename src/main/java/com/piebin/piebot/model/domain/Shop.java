package com.piebin.piebot.model.domain;

import com.piebin.piebot.model.entity.ItemCategory;
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
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    private ItemInfo itemInfo;

    @Builder.Default
    private Long price = 0L;

    @Column(name = "day_count_limit")
    private Long dayCountLImit;

    @Column(name = "week_count_limit")
    private Long weekCountLimit;

    @Column(name = "month_count_limit")
    private Long monthCountLimit;

    @Column(name = "account_count_limit")
    private Long accountCountLimit;

    @Column(name = "total_count_limit")
    private Long totalCountLimit;

    @Column(name = "item_category")
    @Enumerated(value = EnumType.STRING)
    private ItemCategory itemCategory;

    @CreatedDate
    @Column(name = "reg_date")
    private LocalDateTime regDate;
}
