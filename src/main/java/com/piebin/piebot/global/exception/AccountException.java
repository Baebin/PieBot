package com.piebin.piebot.global.exception;

import com.piebin.piebot.global.exception.entity.AccountErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountException extends RuntimeException {
    private AccountErrorCode errorCode;
}
