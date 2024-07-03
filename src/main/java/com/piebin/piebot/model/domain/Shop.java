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
    private Long day_count_limit;

    @Column(name = "week_count_limit")
    private Long week_count_limit;

    @Column(name = "month_count_limit")
    private Long month_count_limit;

    @Column(name = "total_count_limit")
    private Long total_count_limit;

    @Column(name = "item_category")
    @Enumerated(value = EnumType.STRING)
    private ItemCategory itemCategory;

    @CreatedDate
    @Column(name = "reg_date")
    private LocalDateTime regDate;
}
