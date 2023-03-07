package com.padingpading.java.jdk13;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jdk.swing.interop.SwingInterOpUtils;
import org.junit.Test;

/**
 * Java 13 开始你可以使用文本块的方式定义字符串
 */
public class B_String {
    
    @Test
    public void repeat() {
        String str=  "{\n" + "    \"code\": \"00000\",\n" + "    \"success\": true,\n" + "    \"message\": \"成功\",\n"
                + "    \"data\": 1\n" + "}";
        String month = "1";
        //定义方式
        String strJSON = """
                {
                    "code": "00000",
                    "success": true,
                    "message": "成功",
                    "data": 1
                }
                """;
        JSONObject jsonObject = JSON.parseObject(strJSON);
        System.out.println(jsonObject);
    }
}
