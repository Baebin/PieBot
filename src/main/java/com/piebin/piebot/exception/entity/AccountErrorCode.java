package com.piebin.piebot.exception.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountErrorCode {
    NOT_FOUND,
    INVENTORY_NOT_FOUND,
}
