package com.piebin.piebot.exception.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShopErrorCode {
    NOT_FOUND,
    DAY_COUNT_LIMIT, WEEK_COUNT_LIMIT, MONTH_COUNT_LIMIT, TOTAL_COUNT_LIMIT,
    MONEY_LESS,
    INVENTORY_NOT_FOUND,
    MAX_STACK_COUNT_LIMIT,
}
