package com.piebin.piebot.exception;

import com.piebin.piebot.exception.entity.AccountErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountException extends RuntimeException {
    private AccountErrorCode errorCode;
}
