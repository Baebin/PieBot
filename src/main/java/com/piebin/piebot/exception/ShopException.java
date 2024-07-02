package com.piebin.piebot.exception;

import com.piebin.piebot.exception.entity.ShopErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShopException extends RuntimeException {
    private ShopErrorCode errorCode;
}
