//package com.padingpading.java.jdk12;
//
//import org.junit.Test;
//
///**
// * @author libin
// * @description
// * @date 2023-03-06
// */
//public class A_Switch {
//
//    @Test
//    public void repeat() {
//        // jdk12之前 通过传入月份，输出月份所属的季节
//        String day = "may";
//        switch (day) {
//            case "march":
//            case "april":
//            case "may":
//                System.out.println("春天");
//                break;
//            case "june":
//            case "july":
//            case "august":
//                System.out.println("夏天");
//                break;
//            case "september":
//            case "october":
//            case "november":
//                System.out.println("秋天");
//                break;
//            case "december":
//            case "january":
//            case "february":
//                System.out.println("冬天");
//                break;
//        }
//        //通过传入一个月份，输出这个月份对应的季节。简单的功能却写了大量代码，而且每个操作都需要一个 break 来防止 Case 穿透
//        //jdk12优化
//        String season = switch (day) {
//            case "march", "april", "may" -> "春天";
//            case "june", "july", "august" -> "夏天";
//            case "september", "october", "november" -> "秋天";
//            case "december", "january", "february" -> "冬天";
//            default -> {
//                //throw new RuntimeException("day error");
//                               System.out.println("day error");
//                               break "day error";
//            }
//        };
//    System.out.println("当前季节是:" + season);
//    }
//
//}
