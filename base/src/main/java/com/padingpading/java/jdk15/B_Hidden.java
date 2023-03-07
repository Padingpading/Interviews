package com.padingpading.java.jdk15;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Base64;

/**
 * 让开发者可以引入一个无法被其他地方发现使用，
 * 且类的生命周期有限的类。这对运行时动态生成类的使用方式十分有利，可以减少内存占用
 */
public class B_Hidden {
    
        private static String CLASS_INFO = "yv66vgAAADQAFAoAAgADBwAEDAAFAAYBABBqYXZhL2xhbmcvT2JqZWN0AQAGPGluaXQ+AQADKClWCAAIAQAOd3d3LndkYnl0ZS5jb20HAAoBABVjb20vd2RieXRlL0pFUDM3MVRlc3QBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAF0xjb20vd2RieXRlL0pFUDM3MVRlc3Q7AQAGbG9va3VwAQAUKClMamF2YS9sYW5nL1N0cmluZzsBAApTb3VyY2VGaWxlAQAPSkVQMzcxVGVzdC5qYXZhACEACQACAAAAAAACAAEABQAGAAEACwAAAC8AAQABAAAABSq3AAGxAAAAAgAMAAAABgABAAAAAwANAAAADAABAAAABQAOAA8AAAAJABAAEQABAAsAAAAbAAEAAAAAAAMSB7AAAAABAAwAAAAGAAEAAAAEAAEAEgAAAAIAEw==";
        
        public static void main(String[] args) throws Throwable {
            byte[] classInBytes = Base64.getDecoder().decode(CLASS_INFO);
            Class<?> proxy = MethodHandles.lookup()
                    .defineHiddenClass(classInBytes, true, MethodHandles.Lookup.ClassOption.NESTMATE)
                    .lookupClass();
            
            System.out.println(proxy.getName());
            MethodHandle mh = MethodHandles.lookup().findStatic(proxy, "lookup", MethodType.methodType(String.class));
            String result = (String) mh.invokeExact();
            System.out.println(result);
        }
    
    public static class JEP371Test {
        public static String lookup() {
            return "www.wdbyte.com";
        }
    }
}
