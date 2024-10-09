package com.padingpading.java.jdk13;

import org.junit.Test;

/**
 * @author libin
 * @description
 * @date 2023-03-07
 */
public class A_Switch {
    
    // 通过传入月份，输出月份所属的季节
    public static String switchJava12(String month) {
        return switch (month) {
            case "march", "april", "may" -> "春天";
            case "june", "july", "august" -> "夏天";
            case "september", "october", "november" -> "秋天";
            case "december", "january", "february" -> "冬天";
            default -> "month erro";
        };
    }
    
    @Test
    public void repeat() {
        String month = "1";
        String monthdStr = switch (month) {
            case "march", "april", "may":
                yield "春天";
            case "june", "july", "august":
                yield "夏天";
            case "september", "october", "november":
                yield "秋天";
            case "december", "january", "february":
                yield "冬天";
            default:
                yield "month error";
        };
        System.out.println(monthdStr);
    }
}
