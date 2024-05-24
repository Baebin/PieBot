package com.piebin.piebot.utility;

import java.text.NumberFormat;

public class NumberManager {
    public static String getNumber(Long num) {
        return NumberFormat.getInstance().format(num);
    }
}
