package com.padingpading.java.jdk14;

/**
 * Switch 表达式改进从 Java 12 就已经开始了，Java 12 让 switch 支持了 L-> 语法，
 * Java 13 引入了 yield 关键词用于返回结果，但是在 Java 12 和 13 中功能都是预览版的，而在 Java 14 中，正式转正。
 */
public class D_Switch {
    // 通过传入月份，输出月份所属的季节
    public static String switchJava12(String month) {
        return switch (month) {
            case "march", "april", "may"            -> "春天";
            case "june", "july", "august"           -> "夏天";
            case "september", "october", "november" -> "秋天";
            case "december", "january", "february"  -> "冬天";
            default -> "month erro";
        };
    }
    // 通过传入月份，输出月份所属的季节
    public static String switchJava13(String month) {
        return switch (month) {
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
    }
}
