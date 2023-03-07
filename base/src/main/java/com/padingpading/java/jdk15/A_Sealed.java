package com.padingpading.java.jdk15;

/**
 * 在 Java 中如果想让一个类不能被继承和修改，这时我们应该使用 final 关键字对类进行修饰。不过这种要么可以继承，
 * 要么不能继承的机制不够灵活，有些时候我们可能想让某个类可以被某些类型继承，但是又不能随意继承，是做不到的。
 * Java 15 尝试解决这个问题，引入了 sealed 类，被 sealed 修饰的类可以指定子类。这样这个类就只能被指定的类继承。
 *
 *  sealed 修饰的类的机制具有传递性，它的子类必须使用指定的关键字进行修饰，且只能是 final、sealed、non-sealed 三者之一
 */
public class A_Sealed {
    
    //示例：犬类（Dog）只能被牧羊犬（Collie）和田园犬（TuGou）继承，使用 sealed 关键字。
    public  sealed interface Dog permits Collie, TuGou {
        //...
    }
    
    //牧羊犬（Collie）只能被边境牧羊犬（BorderCollie）继承。
    public sealed class Collie implements Dog permits BorderCollie {
    
    }
    
    //边境牧羊犬（BorderCollie）不能被继承，使用 final 关键字。
    public final class BorderCollie extends Collie{
    }
    
    /**
     * @author niulang
     */
    //田园犬（ToGou）可以被任意继承，使用 non-sealed 关键字。
    public non-sealed class TuGou implements Dog {
    
    }
}
