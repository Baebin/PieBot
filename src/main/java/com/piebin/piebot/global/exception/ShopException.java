package com.piebin.piebot.global.exception;

import com.piebin.piebot.global.exception.entity.ShopErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShopException extends RuntimeException {
    private ShopErrorCode errorCode;
}
