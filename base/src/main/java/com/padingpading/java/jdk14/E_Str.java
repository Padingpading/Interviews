package com.padingpading.java.jdk14;

/**
  *  文本块是 Java 13 引入的语法，在 Java 14 中对其进行了增强。文本块依旧是预览功能，这次更新增加了两个转义符。
 */
public class E_Str {
    public static void main(String[] args) {
        String content = """
        {
            "upperSummary": null,\
            "sensitiveTypeList": null,
            "gmtModified": "2011-08-05\s10:50:09",
        }
         """;
        System.out.println(content);
//        {
//            "upperSummary": null,    "sensitiveTypeList": null,
//                "gmtModified": "2011-08-05 10:50:09",
//        }
    }

}
