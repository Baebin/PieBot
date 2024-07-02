package com.piebin.piebot.model.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne
    private Account account;

    @OneToMany
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    public Item findItem(ItemInfo itemInfo) {
        for (Item item : items)
            if (item.getItemInfo().getIdx().equals(itemInfo.getIdx()))
                return item;
        return null;
    }

    public void addItem(Item item) {
        if (items.contains(item))
            return;
        items.add(item);
    }

    public void removeItem(Item item) {
        if (!items.contains(item))
            return;
        items.remove(item);
    }

    @CreatedDate
    @Column(name = "reg_date")
    private LocalDateTime regDate;
}
