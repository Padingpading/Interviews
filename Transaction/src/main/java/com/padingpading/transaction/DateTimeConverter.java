package com.padingpading.transaction;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeConverter {
    // 正则修正格式：补充分钟和时区格式（如 "+0" → "+00:00"）
    private static final Pattern DATE_FIX_PATTERN =
            Pattern.compile("(\\w{3} \\d{2} \\d{4} \\d{2}:)(\\+\\d|\\-\\d)");
    
    // 自定义 DateTimeFormatter（指定 Locale.US 确保月份解析正确）
    private static final DateTimeFormatter FORMATTER =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("MMM dd yyyy HH:mmXXX")
                    .toFormatter(Locale.US);
    
    // 修正原始日期字符串
    private static String fixDateTime(String rawDateTime) {
        Matcher matcher = DATE_FIX_PATTERN.matcher(rawDateTime);
        if (matcher.find()) {
            // 示例修正逻辑："Nov 10 2021 01: +0" → "Nov 10 2021 01:00+00:00"
            return matcher.group(1) + "00" + matcher.group(2) + "00";
        }
        throw new IllegalArgumentException("Invalid datetime format: " + rawDateTime);
    }
    
    // 解析为 LocalDateTime（忽略时区）
    public static LocalDateTime parseToLocalDateTime(String rawDateTime) {
        String fixedDateTime = fixDateTime(rawDateTime);
        return LocalDateTime.parse(fixedDateTime, FORMATTER);
    }
    
    // 解析为 ZonedDateTime（包含时区）
    public static ZonedDateTime parseToZonedDateTime(String rawDateTime) {
        String fixedDateTime = fixDateTime(rawDateTime);
        return ZonedDateTime.parse(fixedDateTime, FORMATTER);
    }
}

