package com.piebin.piebot.utility;

import java.text.NumberFormat;

public class NumberManager {
    public static String getNumber(Long num) {
        return NumberFormat.getInstance().format(num);
    }

    public static String parseString(double num) {
        if (num == (long) num)
            return "" + (int)num;
        return "" + num;
    }
}
