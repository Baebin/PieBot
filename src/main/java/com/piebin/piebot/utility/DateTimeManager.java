package com.piebin.piebot.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeManager {
    public static String getDate(LocalDateTime dateTime) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return dateTime.format(dateFormat);
    }

    public static boolean isBeforeMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime.isBefore(LocalDateTime.now().minusMinutes(minutes));
    }

    public static boolean hasWaitingMinutes(LocalDateTime dateTime, long minutes) {
        if (dateTime == null)
            return false;
        return !isBeforeMinutes(dateTime, minutes);
    }

    public static boolean isToday(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.getYear() != now.getYear())
            return false;
        if (dateTime.getMonth() != now.getMonth())
            return false;
        if (dateTime.getDayOfMonth() != now.getDayOfMonth())
            return false;
        return true;
    }
}
