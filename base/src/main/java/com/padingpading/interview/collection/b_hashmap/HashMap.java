///*
// * Copyright (c) 1997, 2017, Oracle and/or its affiliates. All rights reserved.
// * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// */
//
//package com.padingpading.interview.collection.b_hashmap;
//
//import sun.misc.SharedSecrets;
//
//import java.io.IOException;
//import java.io.InvalidObjectException;
//import java.io.Serializable;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.AbstractCollection;
//import java.util.AbstractMap;
//import java.util.AbstractSet;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.ConcurrentModificationException;
//import java.util.Hashtable;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.NoSuchElementException;
//import java.util.Objects;
//import java.util.Set;
//import java.util.Spliterator;
//import java.util.TreeMap;
//import java.util.function.BiConsumer;
//import java.util.function.BiFunction;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//
///*
//*
//*
//*
//*
//* afterNodeAccess:子类linkedhashmap实现,当节点被访问后,会将该该节点添加到末尾。达到先访问的节点后出现。
//* */
//public class HashMap<K,V> extends AbstractMap<K,V>
//    implements Map<K,V>, Cloneable, Serializable {
//    //序列化号,文件版本。
//    private static final long serialVersionUID = 362498820763181265L;
//
//
//    /**
//     * 默认的table初始容量16
//     */
//    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
//
//    //2的30次
//    //2的31次  需要8位存储数组的长度。
//    //2的31次-8 不符合map的扩容原理。
//    static final int MAXIMUM_CAPACITY = 1 << 30;
//
//    /**默认的负载因子
//     * 当存储的元素>容器的大小*0.75 ,则进行扩容
//     */
//    static final float DEFAULT_LOAD_FACTOR = 0.75f;
//
//    /**table的长度>64，并且链表长度>8，会转为红黑树。
//     */
//    static final int TREEIFY_THRESHOLD = 8;
//    //为什么等于6才会转为链表。删减两个元素之后
//    //7个元素的红黑树,是一个而平衡二叉树,强制解体，失去了它的平衡性。
//    /**红黑树转为链表的阈值, 红黑树长度<6，红黑树转为链表。
//     */
//    static final int UNTREEIFY_THRESHOLD = 6;
//
//    /**最小扩容大小。
//     */
//    static final int MIN_TREEIFY_CAPACITY = 64;
//
//    /**
//     * 容器数组
//     */
//    static class Node<K,V> implements Entry<K,V> {
//        //key的hash,对象的hash生成后是一定的
//        final int hash;
//        //避免对key进行修改。
//        final K key;
//        V value;
//        Node<K,V> next;
//
//        Node(int hash, K key, V value, Node<K,V> next) {
//            this.hash = hash;
//            this.key = key;
//            this.value = value;
//            this.next = next;
//        }
//
//        public final K getKey()        { return key; }
//        public final V getValue()      { return value; }
//        public final String toString() { return key + "=" + value; }
//        //当前node的hashcode的生成。
//        public final int hashCode()
//        {
//            //key的hashcode和value的hahscode 异或操作，更加平均。
//            return Objects.hashCode(key) ^ Objects.hashCode(value);
//        }
//        //value的替换
//        public final V setValue(V newValue) {
//            V oldValue = value;
//            value = newValue;
//            return oldValue;
//        }
//        //比较两个node是否相等
//        public final boolean equals(Object o) {
//            if (o == this)
//              //如果对象相等。同一个对象的内存地址一定是相同的。
//                return true;
//            //node比较必须的集成Entry
//            if (o instanceof Map.Entry) {
//                //比较不同的对象,里面的key值是否相同
//                Entry<?,?> e = (Entry<?,?>)o;
//                //1、比较两个key是否相等。
//                //1.1比较两个key的地址值是否相同。
//                //1.2根据key的equals的方法判断。
//                //2、比较value是否相同。
//                //2.1比较两个value的地址值是否相同。
//                //2.2根据value的equals的方法判断。
//                if (Objects.equals(key, e.getKey()) &&
//                    Objects.equals(value, e.getValue()))
//                    return true;
//            }
//            return false;
//        }
//    }
//
//    /**计算hash值
//     *如果key=null hash = 0,元素i = (n - 1) & hash =  0 ,永远在数组arr[0]的位置
//     *如果key！=null  扰动处理: hashCode 异或 hashcode >>> 16
//     * int hash = 4 * 8 = 32位 大约有21亿长度,
//     * 高16位异或低16位,避免有些hash函数得到的后table-1位全部为0.避免了hash碰撞
//     */
//    /* ---------------- Static utilities -------------- */
//    static final int hash(Object key) {
//        int h;
//        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
//    }
//
//    /**判断 x 是否
//     * 1、实现了Comparable的接口。
//     * 2、
//     * Returns x's Class if it is of the form "class C implements
//     * Comparable<C>", else null.
//     */
//    static Class<?> comparableClassFor(Object x) {
//        if (x instanceof Comparable) {
//            Class<?> c;
//            Type[] ts, as;
//            Type t;
//            ParameterizedType p;//ArrayList<String>
//            if ((c = x.getClass()) == String.class) // bypass checks
//                //如果是String类型直接返回, String自己实现了Comparable接口。
//                return c;
//            //继承的接口不等于null,
//            //[List<Object>,RandomAcess,CloneAble,Seriallizable]
//            if ((ts = c.getGenericInterfaces()) != null) {
//                //遍历接口。
//                for (int i = 0; i < ts.length; ++i) {
//                    //是否是符合类型
//                    if (((t = ts[i]) instanceof ParameterizedType) &&// List<Object>
//                        ((p = (ParameterizedType)t).getRawType() ==//List.class
//                         Comparable.class) &&
//                            //内层元素第一个
//                        (as = p.getActualTypeArguments()) != null &&//[Obejct.class]
//                        as.length == 1 && as[0] == c) // type arg is c
//                        return c;
//                }
//            }
//        }
//        return null;
//    }
//
//    /** K 和X进行比较。
//     * Returns k.compareTo(x) if x matches kc (k's screened comparable
//     * class), else 0.
//     */
//    @SuppressWarnings({"rawtypes","unchecked"}) // for cast to Comparable
//    static int compareComparables(Class<?> kc, Object k, Object x) {
//        return (x == null || x.getClass() != kc ? 0 :
//                ((Comparable)k).compareTo(x));
//    }
//
//    /**取出一个数字的最接近2的n次方,向上去
//     * Returns a power of two size for the given target capacity.
//     */
//    static final int tableSizeFor(int cap) {
//        int n = cap - 1;//9-1001
//        n |= n >>> 1;//1001|0100=1101
//        n |= n >>> 2;//1100|0011=1111
//        n |= n >>> 4;//1111
//        n |= n >>> 8;//1111
//        n |= n >>> 16;//1111
//        //n+1 = 10000
//        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
//    }
//
//    /* ---------------- Fields -------------- */
//
//    /**容器 node的数组。
//     * The table, initialized on first use, and resized as
//     * necessary. When allocated, length is always a power of two.
//     * (We also tolerate length zero in some operations to allow
//     * bootstrapping mechanics that are currently not needed.)
//     */
//    transient Node<K,V>[] table;
//
//    /**
//     * Holds cached entrySet(). Note that AbstractMap fields are used
//     * for keySet() and values().
//     */
//    transient Set<Entry<K,V>> entrySet;
//
//    /**当前hashmap包含的键值对的数量
//     * The number of key-value mappings contained in this map.
//     */
//    transient int size;
//
//    /**
//     * 当前hashmap修改的次数
//     */
//    transient int modCount;
//
//    //当前hashmap才能称号搜的最多键值对数量,一旦超过这个数量hashmap就会扩容。
//    int threshold;
//
//    /**
//     * 负载因子,用于扩容
//     */
//    final float loadFactor;
//
//    /* ---------------- Public operations -------------- */
//
//    /**
//     * Constructs an empty <tt>HashMap</tt> with the specified initial
//     * capacity and load factor.
//     *
//     * @param  initialCapacity the initial capacity
//     * @param  loadFactor      the load factor
//     * @throws IllegalArgumentException if the initial capacity is negative
//     *         or the load factor is nonpositive
//     */
//    public HashMap(int initialCapacity, float loadFactor) {
//        //小于0
//        if (initialCapacity < 0)
//            throw new IllegalArgumentException("Illegal initial capacity: " +
//                                               initialCapacity);
//        //大于2的30次方
//        if (initialCapacity > MAXIMUM_CAPACITY)
//            initialCapacity = MAXIMUM_CAPACITY;
//        //校验loadFactory
//        if (loadFactor <= 0 || Float.isNaN(loadFactor))
//            throw new IllegalArgumentException("Illegal load factor: " +
//                                               loadFactor);
//        //赋值
//        this.loadFactor = loadFactor;
//        //初始化容量,获取最近接近2的n次方的数字
//        this.threshold = tableSizeFor(initialCapacity);
//    }
//
//    /**
//     * Constructs an empty <tt>HashMap</tt> with the specified initial
//     * capacity and the default load factor (0.75).
//     *
//     * @param  initialCapacity the initial capacity.
//     * @throws IllegalArgumentException if the initial capacity is negative.
//     */
//    public HashMap(int initialCapacity) {
//        //调用上面的方法,使用默认的负载因子0.75
//        this(initialCapacity, DEFAULT_LOAD_FACTOR);
//    }
//
//    /**
//     * Constructs an empty <tt>HashMap</tt> with the default initial capacity
//     * (16) and the default load factor (0.75).
//     */
//    public HashMap() {
//        //默认负载因子0.75,没有对初始化容量赋值
//        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
//    }
//
//    /**
//     * Constructs a new <tt>HashMap</tt> with the same mappings as the
//     * specified <tt>Map</tt>.  The <tt>HashMap</tt> is created with
//     * default load factor (0.75) and an initial capacity sufficient to
//     * hold the mappings in the specified <tt>Map</tt>.
//     *
//     * @param   m the map whose mappings are to be placed in this map
//     * @throws  NullPointerException if the specified map is null
//     */
//    //
//    public HashMap(Map<? extends K, ? extends V> m) {
//        //默认负载因子0.75
//        this.loadFactor = DEFAULT_LOAD_FACTOR;
//        //添加外部的map
//        putMapEntries(m, false);
//    }
//
//    /**
//     * Implements Map.putAll and Map constructor
//     *
//     * @param m the map
//     * @param evict false when initially constructing this map, else
//     * true (relayed to method afterNodeInsertion).
//     */
//    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
//        //外部map的size
//        int s = m.size();
//        if (s > 0) {
//            //反向计算新map的容量
//            if (table == null) { // pre-size
//                //size/0.75+1
//                float ft = ((float)s / loadFactor) + 1.0F;
//                //转为int
//                int t = ((ft < (float)MAXIMUM_CAPACITY) ?
//                         (int)ft : MAXIMUM_CAPACITY);
//                //得到容量t的最近接2的n次的数字。
//                if (t > threshold)
//                    threshold = tableSizeFor(t);
//            }
//            else if (s > threshold)
//                //存储的size大于容量的容量,进行扩容。
//                resize();
//            //遍历map
//            for (Entry<? extends K, ? extends V> e : m.entrySet()) {
//                K key = e.getKey();
//                V value = e.getValue();
//                //添加到当前的map中。
//                putVal(hash(key), key, value, false, evict);
//            }
//        }
//    }
//
//    /**
//     * Returns the number of key-value mappings in this map.
//     *
//     * @return the number of key-value mappings in this map
//     */
//    public int size() {
//        return size;
//    }
//
//    /**
//     * Returns <tt>true</tt> if this map contains no key-value mappings.
//     *
//     * @return <tt>true</tt> if this map contains no key-value mappings
//     */
//    public boolean isEmpty() {
//        return size == 0;
//    }
//
//    /**
//     * Returns the value to which the specified key is mapped,
//     * or {@code null} if this map contains no mapping for the key.
//     *
//     * <p>More formally, if this map contains a mapping from a key
//     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
//     * key.equals(k))}, then this method returns {@code v}; otherwise
//     * it returns {@code null}.  (There can be at most one such mapping.)
//     *
//     * <p>A return value of {@code null} does not <i>necessarily</i>
//     * indicate that the map contains no mapping for the key; it's also
//     * possible that the map explicitly maps the key to {@code null}.
//     * The {@link #containsKey containsKey} operation may be used to
//     * distinguish these two cases.
//     *
//     * @see #put(Object, Object)
//     */
//    public V get(Object key) {
//        Node<K,V> e;
//        return (e = getNode(hash(key), key)) == null ? null : e.value;
//    }
//
//    /**
//     * Implements Map.get and related methods
//     *
//     * @param hash hash for key
//     * @param key the key
//     * @return the node, or null if none
//     */
//    //hash=key的hash右移16位,key:键
//    final Node<K,V> getNode(int hash, Object key) {
//        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
//        //1、判断是有数据。
//        //(Object.hashCode^Object.hashcode()>>>16)&(table.length-1）
//        //2、hash&table.length-1 = 元素在数组上的位置。
//        //3、
//        if ((tab = table) != null && (n = tab.length) > 0 &&
//            (first = tab[(n - 1) & hash]) != null) {
//            //first节点,并且key相同,先比较地址值,后比较equals。
//            if (first.hash == hash && // always check first node
//                ((k = first.key) == key || (key != null && key.equals(k))))
//                //返回
//                return first;
//            //查找下一个节点
//            if ((e = first.next) != null) {
//                //红黑树
//                if (first instanceof TreeNode)
//                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
//                do {
//                    //链表循环。如果hash值相同,equals 不一定相同,如果hash不同,equals一定不同。
//                    if (e.hash == hash &&
//                        ((k = e.key) == key || (key != null && key.equals(k))))
//                        return e;
//                } while ((e = e.next) != null);
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Returns <tt>true</tt> if this map contains a mapping for the
//     * specified key.
//     *
//     * @param   key   The key whose presence in this map is to be tested
//     * @return <tt>true</tt> if this map contains a mapping for the specified
//     * key.
//     */
//    public boolean containsKey(Object key) {
//        return getNode(hash(key), key) != null;
//    }
//
//    /**
//     * Associates the specified value with the specified key in this map.
//     * If the map previously contained a mapping for the key, the old
//     * value is replaced.
//     *
//     * @param key key with which the specified value is to be associated
//     * @param value value to be associated with the specified key
//     * @return the previous value associated with <tt>key</tt>, or
//     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
//     *         (A <tt>null</tt> return can also indicate that the map
//     *         previously associated <tt>null</tt> with <tt>key</tt>.)
//     */
//    public V put(K key, V value) {
//        return putVal(hash(key), key, value, false, true);
//    }
//
//    /**
//     * Implements Map.put and related methods
//     *
//     * @param hash hash for key
//     * @param key the key
//     * @param value the value to put
//     * @param onlyIfAbsent if true, don't change existing value
//     * @param evict if false, the table is in creation mode.
//     * @return previous value, or null if none
//     */
//    //如果一个hashmap宗有1000个元素,我们可以通过put方法的返回值,进行一个是否有数值覆盖的判断。
//    //如果有的业务逻辑从上层业务需求上,就可以肯定lkey是绝对唯一的。那么这个时候,如果我们使hashmap
//    //进行数据的维护添加的话,好的代码需要使用这个特性,来保证我们业务的key确实是唯一的。
//    //如果发现我们的put方法返回有值得花,那说业务的key不唯一了,可以根据业务需求,直接抛出异常.
//    //通过代码判断,进行二次的校验。
//    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
//                   boolean evict) {
//        Node<K,V>[] tab; Node<K,V> p; int n, i;
//        //table如果等于0,resize扩容。
//        if ((tab = table) == null || (n = tab.length) == 0)
//            //扩容后的长度赋值给n
//            n = (tab = resize()).length;
//        //定位到key的槽位 为null,说明没有数据  (n - 1) & hash 相当于取模
//        if ((p = tab[i = (n - 1) & hash]) == null)
//            //创建node复制到槽位。LinekedHashmap进行复写
//            tab[i] = newNode(hash, key, value, null);
//        else {
//            //当前数组槽位有数据。
//            //put后返回的元素,原来该位置的元素。链条的
//            Node<K,V> e;
//            K k;
//            //p为当前槽位的首个元素,判断加入的key是否是首个元素。
//            if (p.hash == hash &&
//                ((k = p.key) == key || (key != null && key.equals(k))))
//                e = p;
//            else if (p instanceof TreeNode)
//                //红黑树。
//                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
//            else {
//                //链表
//                for (int binCount = 0; ; ++binCount) {
//                    //循环链表。
//                    if ((e = p.next) == null) {
//                        //尾插法。赋值新node。LinkedHashmap进行复写
//                        p.next = newNode(hash, key, value, null);
//                        //bingcount>=7 转化为树,实际为 链表长度>=8
//                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
//                            treeifyBin(tab, hash);
//                        break;
//                    }
//                    //添加的key相同,
//                    if (e.hash == hash &&
//                        ((k = e.key) == key || (key != null && key.equals(k))))
//                        break;
//                    //
//                    p = e;
//                }
//            }
//            //e存在,添加的key是存在的。
//            if (e != null) { // existing mapping for key
//                //替换value。
//                V oldValue = e.value;
//                //onlyIfAbsent 如果有值，则不进行覆盖。
//                if (!onlyIfAbsent || oldValue == null)
//                    e.value = value;
//                //linkedhashmap使用,做插入和访问顺序的控制。
//                afterNodeAccess(e);
//                //返回旧值。
//                return oldValue;
//            }
//        }
//        ++modCount;
//        //先添加,后扩容.
//        if (++size > threshold)
//            resize();
//        afterNodeInsertion(evict);
//        return null;
//    }
//
//    /**
//     * Initializes or doubles table size.  If null, allocates in
//     * accord with initial capacity target held in field threshold.
//     * Otherwise, because we are using power-of-two expansion, the
//     * elements from each bin must either stay at same index, or move
//     * with a power of two offset in the new table.
//     *
//     * @return the table
//     */
//    final Node<K,V>[] resize() {
//        Node<K,V>[] oldTab = table;
//        //old table=16
//        int oldCap = (oldTab == null) ? 0 : oldTab.length;
//        //阈值数量=16*0.75=12
//        int oldThr = threshold;
//        int newCap, newThr = 0;
//        if (oldCap > 0) {
//            //table大于最大值。
//            if (oldCap >= MAXIMUM_CAPACITY) {
//                //放弃扩容,阈值就是最大值。
//                threshold = Integer.MAX_VALUE;
//                return oldTab;
//            }
//            //扩容后的阈值<最大 并且 现有的长度>默认初始化大小16
//            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
//                     oldCap >= DEFAULT_INITIAL_CAPACITY)
//                //数组长度阈值*2
//                newThr = oldThr << 1; // double threshold
//        }
//        else if (oldThr > 0) // initial capacity was placed in threshold
//            //数组长度=0,并且阈值>0 新容器的大小为阈值。
//            newCap = oldThr;
//        else {
//            //oldcap=0 threshold=0
//            // zero initial threshold signifies using defaults
//            //新容器的大小16
//            newCap = DEFAULT_INITIAL_CAPACITY;
//            //新的阈值大小=16*0.75=12
//            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
//        }
//        if (newThr == 0) {
//            float ft = (float)newCap * loadFactor;
//            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
//                      (int)ft : Integer.MAX_VALUE);
//        }
//        //赋值给Hashmap.threshold
//        //第一次 12
//        //第二次 23
//        threshold = newThr;
//        //创建node数组,赋值给HashMapd.table
//        //第一次 16
//        //第二次 32
//        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
//        table = newTab;
//        //开始节点迁移
//        if (oldTab != null) {
//            //遍历老table数组
//            for (int j = 0; j < oldCap; ++j) {
//                Node<K,V> e;
//                //将头元素赋值给e
//                if ((e = oldTab[j]) != null) {
//                    //老的table置位null。
//                    oldTab[j] = null;
//                    if (e.next == null)
//                        //单个节点,非链表,直接添加。
//                        newTab[e.hash & (newCap - 1)] = e;
//                    else if (e instanceof TreeNode)
//                        //红黑树。如果进行map扩容,我们的书肯定会数拆掉，性能消耗。
//                        //实际情况下,hashmap很难达到红黑树,hash十分散列。
//                        //如果我们需要从db里去除2的30多次放数据,需要hashmao进行处理怎么办。
//                        //可以用一个map分批处理,一次处理完之后clear()，避免扩容。
//                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
//                    else { // preserve order
//                        //链表结构。
//                        //低八位
//                        Node<K,V> loHead = null,//old链表 头结点
//                                  loTail = null;//old链表 老节点
//                        //高八位
//                        Node<K,V> hiHead = null,//新链表的头结点
//                                  hiTail = null;//新链表的为节点
//                        Node<K,V> next;
//                        do {
//                            //头结点的下一个结点
//                            next = e.next;
//                            //元素的hash & oldCap.lenth =0 d位置不变
//                            //oldCap为新的容器的大小。
//                            if ((e.hash & oldCap) == 0) {
//                                if (loTail == null)
//                                    //赋值head结点
//                                    loHead = e;
//                                else
//                                    //尾插法
//                                    loTail.next = e;
//                                loTail = e;
//                            }
//                            //如果不等于0,变化位置,将该元素放置到新的table
//                            else {
//                                //尾插法
//                                if (hiTail == null)
//                                    hiHead = e;
//                                else
//                                    hiTail.next = e;
//                                hiTail = e;
//                            }
//                            //知道遍历完链表。
//                        } while ((e = next) != null);
//                        //赋值到低八位
//                        if (loTail != null) {
//                            loTail.next = null;
//                            newTab[j] = loHead;
//                        }
//                        //复制到高八位。
//                        if (hiTail != null) {
//                            hiTail.next = null;
//                            newTab[j + oldCap] = hiHead;
//                        }
//                    }
//                }
//            }
//        }
//        return newTab;
//    }
//
//    /**
//     *
//     */
//    final void treeifyBin(Node<K,V>[] tab, int hash) {
//        int n, index; Node<K,V> e;
//        //数组的容量<64,选择进行扩容。否则转红黑树。
//        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
//            resize();
//        else if ((e = tab[index = (n - 1) & hash]) != null) {
//            //数组上要转化的链表,e是链表的头结点
//            TreeNode<K,V> hd = null, tl = null;
//            //循环:遍历链表节点,转化为 treenode之做以后关联。
//            do {
//                //创建树,TreeNode<-entry<node，这里实际上市给node赋值。
//                TreeNode<K,V> p = replacementTreeNode(e, null);
//                if (tl == null)
//                    //只会进来一次,赋值给hd
//                    hd = p;
//                else {
//                    p.prev = tl;
//                    tl.next = p;
//                }
//                tl = p;
//            } while ((e = e.next) != null);
//            if ((tab[index] = hd) != null)
//                //转化为红黑树。
//                hd.treeify(tab);
//        }
//    }
//
//    public void putAll(Map<? extends K, ? extends V> m) {
//        putMapEntries(m, true);
//    }
//
//    /**
//     * 删除一个节点,返回节点value。
//     */
//    public V remove(Object key) {
//        Node<K,V> e;
//        return (e = removeNode(hash(key), key, null, false, true)) == null ?
//            null : e.value;
//    }
//
//
//    /**清除node
//     * @param hash
//     * @param key
//     * @param value
//     * @param matchValue value相同才会做清除
//     * @param movable
//     * @return
//     */
//    final Node<K,V> removeNode(int hash, Object key, Object value,
//                               boolean matchValue, boolean movable) {
//        Node<K,V>[] tab; Node<K,V> p; int n, index;
//        //table[index = (n - 1) & hash]不等于空,并且将第一个元素赋给p
//        if ((tab = table) != null && (n = tab.length) > 0 &&
//            (p = tab[index = (n - 1) & hash]) != null) {
//            Node<K,V> node = null, e; K k; V v;
//            //判断节点是否相同。
//            if (p.hash == hash &&
//                ((k = p.key) == key || (key != null && key.equals(k))))
//                node = p;
//            else if ((e = p.next) != null) {
//                //如果是红黑树,红黑树的父类是Node
//                if (p instanceof TreeNode)
//                    node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
//                else {
//                    //链表结构,先do-while结构,前面已经判断了头元素一定会有元素,避免再次进入循环体。
//                    do {
//                        if (e.hash == hash &&
//                            ((k = e.key) == key ||
//                             (key != null && key.equals(k)))) {
//                            node = e;
//                            break;
//                        }
//                        p = e;
//                    } while ((e = e.next) != null);
//                }
//            }
//            //node删减
//            //1、node存在
//            //2、如果matchValue==false,这里恒等于true
//            //3、
//            if (node != null && (!matchValue || (v = node.value) == value ||
//                                 (value != null && value.equals(v)))) {
//                if (node instanceof TreeNode)
//                    ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
//                else if (node == p)
//                    //链表头结点
//                    tab[index] = node.next;
//                else
//                    //链表非头结点
//                    p.next = node.next;
//                ++modCount;
//                --size;
//                //删除节点后,在linkedhashmap删除节点。
//                afterNodeRemoval(node);
//                return node;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * map清空
//     */
//    public void clear() {
//        Node<K,V>[] tab;
//        modCount++;
//        if ((tab = table) != null && size > 0) {
//            size = 0;
//            for (int i = 0; i < tab.length; ++i)
//                //直接设置数组为null
//                tab[i] = null;
//        }
//    }
//
//    /**
//     * 是否包含value。
//     */
//    public boolean containsValue(Object value) {
//        Node<K,V>[] tab; V v;
//        //table!=null
//        if ((tab = table) != null && size > 0) {
//            //遍历table
//            for (int i = 0; i < tab.length; ++i) {
//                //遍历链表。直接是比对的链表,没有比对红黑树？
//                for (Node<K,V> e = tab[i]; e != null; e = e.next) {
//                    if ((v = e.value) == value ||
//                        (value != null && value.equals(v)))
//                        return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     *abstractMap中的ketset
//     * hashmap中key的映射,.其实真正的keyset并不是实际意义的。keyset里面的元素
//     * 其实都是对hashmap里面key的内存地址的一个映射。？
//     */
//    public Set<K> keySet() {
//        Set<K> ks = keySet;
//        if (ks == null) {
//            ks = new KeySet();
//            keySet = ks;
//        }
//        return ks;
//    }
//
//    final class KeySet extends AbstractSet<K> {
//        public final int size()                 { return size; }
//        public final void clear()               { HashMap.this.clear(); }
//        public final Iterator<K> iterator()     { return new KeyIterator(); }
//        public final boolean contains(Object o) { return containsKey(o); }
//        public final boolean remove(Object key) {
//            return removeNode(hash(key), key, null, false, true) != null;
//        }
//        public final Spliterator<K> spliterator() {
//            return new KeySpliterator<>(HashMap.this, 0, -1, 0, 0);
//        }
//        public final void forEach(Consumer<? super K> action) {
//            Node<K,V>[] tab;
//            if (action == null)
//                throw new NullPointerException();
//            if (size > 0 && (tab = table) != null) {
//                int mc = modCount;
//                for (int i = 0; i < tab.length; ++i) {
//                    for (Node<K,V> e = tab[i]; e != null; e = e.next)
//                        action.accept(e.key);
//                }
//                if (modCount != mc)
//                    throw new ConcurrentModificationException();
//            }
//        }
//    }
//
//    /**
//     * Returns a {@link Collection} view of the values contained in this map.
//     * The collection is backed by the map, so changes to the map are
//     * reflected in the collection, and vice-versa.  If the map is
//     * modified while an iteration over the collection is in progress
//     * (except through the iterator's own <tt>remove</tt> operation),
//     * the results of the iteration are undefined.  The collection
//     * supports element removal, which removes the corresponding
//     * mapping from the map, via the <tt>Iterator.remove</tt>,
//     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
//     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
//     * support the <tt>add</tt> or <tt>addAll</tt> operations.
//     *
//     * @return a view of the values contained in this map
//     */
//    public Collection<V> values() {
//        Collection<V> vs = values;
//        if (vs == null) {
//            vs = new Values();
//            values = vs;
//        }
//        return vs;
//    }
//
//    final class Values extends AbstractCollection<V> {
//        public final int size()                 { return size; }
//        public final void clear()               { HashMap.this.clear(); }
//        public final Iterator<V> iterator()     { return new ValueIterator(); }
//        public final boolean contains(Object o) { return containsValue(o); }
//        public final Spliterator<V> spliterator() {
//            return new ValueSpliterator<>(HashMap.this, 0, -1, 0, 0);
//        }
//        public final void forEach(Consumer<? super V> action) {
//            Node<K,V>[] tab;
//            if (action == null)
//                throw new NullPointerException();
//            if (size > 0 && (tab = table) != null) {
//                int mc = modCount;
//                for (int i = 0; i < tab.length; ++i) {
//                    for (Node<K,V> e = tab[i]; e != null; e = e.next)
//                        action.accept(e.value);
//                }
//                if (modCount != mc)
//                    throw new ConcurrentModificationException();
//            }
//        }
//    }
//
//    /**
//     * Returns a {@link Set} view of the mappings contained in this map.
//     * The set is backed by the map, so changes to the map are
//     * reflected in the set, and vice-versa.  If the map is modified
//     * while an iteration over the set is in progress (except through
//     * the iterator's own <tt>remove</tt> operation, or through the
//     * <tt>setValue</tt> operation on a map entry returned by the
//     * iterator) the results of the iteration are undefined.  The set
//     * supports element removal, which removes the corresponding
//     * mapping from the map, via the <tt>Iterator.remove</tt>,
//     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
//     * <tt>clear</tt> operations.  It does not support the
//     * <tt>add</tt> or <tt>addAll</tt> operations.
//     *
//     * @return a set view of the mappings contained in this map
//     */
//    public Set<Entry<K,V>> entrySet() {
//        Set<Entry<K,V>> es;
//        return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
//    }
//
//    final class EntrySet extends AbstractSet<Entry<K,V>> {
//        public final int size()                 { return size; }
//        public final void clear()               { HashMap.this.clear(); }
//        public final Iterator<Entry<K,V>> iterator() {
//            return new EntryIterator();
//        }
//        public final boolean contains(Object o) {
//            if (!(o instanceof Map.Entry))
//                return false;
//            Entry<?,?> e = (Entry<?,?>) o;
//            Object key = e.getKey();
//            Node<K,V> candidate = getNode(hash(key), key);
//            return candidate != null && candidate.equals(e);
//        }
//        public final boolean remove(Object o) {
//            if (o instanceof Map.Entry) {
//                Entry<?,?> e = (Entry<?,?>) o;
//                Object key = e.getKey();
//                Object value = e.getValue();
//                return removeNode(hash(key), key, value, true, true) != null;
//            }
//            return false;
//        }
//        public final Spliterator<Entry<K,V>> spliterator() {
//            return new EntrySpliterator<>(HashMap.this, 0, -1, 0, 0);
//        }
//        public final void forEach(Consumer<? super Entry<K,V>> action) {
//            Node<K,V>[] tab;
//            if (action == null)
//                throw new NullPointerException();
//            if (size > 0 && (tab = table) != null) {
//                int mc = modCount;
//                for (int i = 0; i < tab.length; ++i) {
//                    for (Node<K,V> e = tab[i]; e != null; e = e.next)
//                        action.accept(e);
//                }
//                if (modCount != mc)
//                    throw new ConcurrentModificationException();
//            }
//        }
//    }
//
//
//    //map中的getOrDefault
////    default V getOrDefault(Object key, V defaultValue) {
////        V v;
////        return (((v = get(key)) != null) || containsKey(key))
////                ? v
////                : defaultValue;
////    }
//    /**
//     * hashmap对mapgetOrDefault中的进行重写
//     */
//    @Override
//    public V getOrDefault(Object key, V defaultValue) {
//        Node<K,V> e;
//        return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
//    }
//
//    /**
//     * 如果key存在,并且value不等于null,不覆盖
//     * 如果key存在,并且value等于null,覆盖
//     * 如果key不存在,覆盖
//     */
//    @Override
//    public V putIfAbsent(K key, V value) {
//        return putVal(hash(key), key, value, true, true);
//    }
//
//    /**
//     * key和value 相同才会进行清除
//     */
//    @Override
//    public boolean remove(Object key, Object value) {
//        return removeNode(hash(key), key, value, true, true) != null;
//    }
//
//    /**替换纸
//     * @param key
//     * @param oldValue 旧值
//     * @param newValue 心智
//     * @return
//     */
//    @Override
//    public boolean replace(K key, V oldValue, V newValue) {
//        Node<K,V> e; V v;
//        //获取node
//        if ((e = getNode(hash(key), key)) != null &&
//                //旧值相等,替换
//            ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))) {
//            e.value = newValue;
//            afterNodeAccess(e);
//            return true;
//        }
//        return false;
//    }
//
//    /**替换纸
//     * @param key
//     * @param value 新值
//     * @return
//     */
//    @Override
//    public V replace(K key, V value) {
//        Node<K,V> e;
//        //获取节点
//        if ((e = getNode(hash(key), key)) != null) {
//            V oldValue = e.value;
//            e.value = value;
//            afterNodeAccess(e);
//            return oldValue;
//        }
//        return null;
//    }
//
//    @Override
//    public V computeIfAbsent(K key,
//                             Function<? super K, ? extends V> mappingFunction) {
//        if (mappingFunction == null)
//            throw new NullPointerException();
//        int hash = hash(key);
//        Node<K,V>[] tab; Node<K,V> first; int n, i;
//        int binCount = 0;
//        TreeNode<K,V> t = null;
//        Node<K,V> old = null;
//        if (size > threshold || (tab = table) == null ||
//            (n = tab.length) == 0)
//            n = (tab = resize()).length;
//        if ((first = tab[i = (n - 1) & hash]) != null) {
//            if (first instanceof TreeNode)
//                old = (t = (TreeNode<K,V>)first).getTreeNode(hash, key);
//            else {
//                Node<K,V> e = first; K k;
//                do {
//                    if (e.hash == hash &&
//                        ((k = e.key) == key || (key != null && key.equals(k)))) {
//                        old = e;
//                        break;
//                    }
//                    ++binCount;
//                } while ((e = e.next) != null);
//            }
//            V oldValue;
//            if (old != null && (oldValue = old.value) != null) {
//                afterNodeAccess(old);
//                return oldValue;
//            }
//        }
//        V v = mappingFunction.apply(key);
//        if (v == null) {
//            return null;
//        } else if (old != null) {
//            old.value = v;
//            afterNodeAccess(old);
//            return v;
//        }
//        else if (t != null)
//            t.putTreeVal(this, tab, hash, key, v);
//        else {
//            tab[i] = newNode(hash, key, v, first);
//            if (binCount >= TREEIFY_THRESHOLD - 1)
//                treeifyBin(tab, hash);
//        }
//        ++modCount;
//        ++size;
//        afterNodeInsertion(true);
//        return v;
//    }
//
//    public V computeIfPresent(K key,
//                              BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
//        if (remappingFunction == null)
//            throw new NullPointerException();
//        Node<K,V> e; V oldValue;
//        int hash = hash(key);
//        if ((e = getNode(hash, key)) != null &&
//            (oldValue = e.value) != null) {
//            V v = remappingFunction.apply(key, oldValue);
//            if (v != null) {
//                e.value = v;
//                afterNodeAccess(e);
//                return v;
//            }
//            else
//                removeNode(hash, key, null, false, true);
//        }
//        return null;
//    }
//
//    @Override
//    public V compute(K key,
//                     BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
//        if (remappingFunction == null)
//            throw new NullPointerException();
//        int hash = hash(key);
//        Node<K,V>[] tab; Node<K,V> first; int n, i;
//        int binCount = 0;
//        TreeNode<K,V> t = null;
//        Node<K,V> old = null;
//        if (size > threshold || (tab = table) == null ||
//            (n = tab.length) == 0)
//            n = (tab = resize()).length;
//        if ((first = tab[i = (n - 1) & hash]) != null) {
//            if (first instanceof TreeNode)
//                old = (t = (TreeNode<K,V>)first).getTreeNode(hash, key);
//            else {
//                Node<K,V> e = first; K k;
//                do {
//                    if (e.hash == hash &&
//                        ((k = e.key) == key || (key != null && key.equals(k)))) {
//                        old = e;
//                        break;
//                    }
//                    ++binCount;
//                } while ((e = e.next) != null);
//            }
//        }
//        V oldValue = (old == null) ? null : old.value;
//        V v = remappingFunction.apply(key, oldValue);
//        if (old != null) {
//            if (v != null) {
//                old.value = v;
//                afterNodeAccess(old);
//            }
//            else
//                removeNode(hash, key, null, false, true);
//        }
//        else if (v != null) {
//            if (t != null)
//                t.putTreeVal(this, tab, hash, key, v);
//            else {
//                tab[i] = newNode(hash, key, v, first);
//                if (binCount >= TREEIFY_THRESHOLD - 1)
//                    treeifyBin(tab, hash);
//            }
//            ++modCount;
//            ++size;
//            afterNodeInsertion(true);
//        }
//        return v;
//    }
//
//    @Override
//    public V merge(K key, V value,
//                   BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
//        if (value == null)
//            throw new NullPointerException();
//        if (remappingFunction == null)
//            throw new NullPointerException();
//        int hash = hash(key);
//        Node<K,V>[] tab; Node<K,V> first; int n, i;
//        int binCount = 0;
//        TreeNode<K,V> t = null;
//        Node<K,V> old = null;
//        if (size > threshold || (tab = table) == null ||
//            (n = tab.length) == 0)
//            n = (tab = resize()).length;
//        if ((first = tab[i = (n - 1) & hash]) != null) {
//            if (first instanceof TreeNode)
//                old = (t = (TreeNode<K,V>)first).getTreeNode(hash, key);
//            else {
//                Node<K,V> e = first; K k;
//                do {
//                    if (e.hash == hash &&
//                        ((k = e.key) == key || (key != null && key.equals(k)))) {
//                        old = e;
//                        break;
//                    }
//                    ++binCount;
//                } while ((e = e.next) != null);
//            }
//        }
//        if (old != null) {
//            V v;
//            if (old.value != null)
//                v = remappingFunction.apply(old.value, value);
//            else
//                v = value;
//            if (v != null) {
//                old.value = v;
//                afterNodeAccess(old);
//            }
//            else
//                removeNode(hash, key, null, false, true);
//            return v;
//        }
//        if (value != null) {
//            if (t != null)
//                t.putTreeVal(this, tab, hash, key, value);
//            else {
//                tab[i] = newNode(hash, key, value, first);
//                if (binCount >= TREEIFY_THRESHOLD - 1)
//                    treeifyBin(tab, hash);
//            }
//            ++modCount;
//            ++size;
//            afterNodeInsertion(true);
//        }
//        return value;
//    }
//
//    @Override
//    public void forEach(BiConsumer<? super K, ? super V> action) {
//        Node<K,V>[] tab;
//        if (action == null)
//            throw new NullPointerException();
//        if (size > 0 && (tab = table) != null) {
//            int mc = modCount;
//            for (int i = 0; i < tab.length; ++i) {
//                for (Node<K,V> e = tab[i]; e != null; e = e.next)
//                    action.accept(e.key, e.value);
//            }
//            if (modCount != mc)
//                throw new ConcurrentModificationException();
//        }
//    }
//
//    @Override
//    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
//        Node<K,V>[] tab;
//        if (function == null)
//            throw new NullPointerException();
//        if (size > 0 && (tab = table) != null) {
//            int mc = modCount;
//            for (int i = 0; i < tab.length; ++i) {
//                for (Node<K,V> e = tab[i]; e != null; e = e.next) {
//                    e.value = function.apply(e.key, e.value);
//                }
//            }
//            if (modCount != mc)
//                throw new ConcurrentModificationException();
//        }
//    }
//
//    /* ------------------------------------------------------------ */
//    // Cloning and serialization
//
//    /**
//     * 浅拷贝
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    public Object clone() {
//        HashMap<K,V> result;
//        try {
//            result = (HashMap<K,V>)super.clone();
//        } catch (CloneNotSupportedException e) {
//            // this shouldn't happen, since we are Cloneable
//            throw new InternalError(e);
//        }
//        //重置map
//        result.reinitialize();
//        //设置map.
//        result.putMapEntries(this, false);
//        return result;
//    }
//
//    final float loadFactor() { return loadFactor; }
//    final int capacity() {
//        return (table != null) ? table.length :
//            (threshold > 0) ? threshold :
//            DEFAULT_INITIAL_CAPACITY;
//    }
//
//    /**
//     * 序列化
//     */
//    private void writeObject(java.io.ObjectOutputStream s)
//        throws IOException {
//        int buckets = capacity();
//        s.defaultWriteObject();
//        s.writeInt(buckets);
//        s.writeInt(size);
//        internalWriteEntries(s);
//    }
//
//    /**
//     * 反序列化
//     */
//    private void readObject(java.io.ObjectInputStream s)
//        throws IOException, ClassNotFoundException {
//        // Read in the threshold (ignored), loadfactor, and any hidden stuff
//        s.defaultReadObject();
//        reinitialize();
//        if (loadFactor <= 0 || Float.isNaN(loadFactor))
//            throw new InvalidObjectException("Illegal load factor: " +
//                                             loadFactor);
//        s.readInt();                // Read and ignore number of buckets
//        int mappings = s.readInt(); // Read number of mappings (size)
//        if (mappings < 0)
//            throw new InvalidObjectException("Illegal mappings count: " +
//                                             mappings);
//        else if (mappings > 0) { // (if zero, use defaults)
//            // Size the table using given load factor only if within
//            // range of 0.25...4.0
//            float lf = Math.min(Math.max(0.25f, loadFactor), 4.0f);
//            float fc = (float)mappings / lf + 1.0f;
//            int cap = ((fc < DEFAULT_INITIAL_CAPACITY) ?
//                       DEFAULT_INITIAL_CAPACITY :
//                       (fc >= MAXIMUM_CAPACITY) ?
//                       MAXIMUM_CAPACITY :
//                       tableSizeFor((int)fc));
//            float ft = (float)cap * lf;
//            threshold = ((cap < MAXIMUM_CAPACITY && ft < MAXIMUM_CAPACITY) ?
//                         (int)ft : Integer.MAX_VALUE);
//
//            // Check Map.Entry[].class since it's the nearest public type to
//            // what we're actually creating.
//            SharedSecrets.getJavaOISAccess().checkArray(s, Entry[].class, cap);
//            @SuppressWarnings({"rawtypes","unchecked"})
//            Node<K,V>[] tab = (Node<K,V>[])new Node[cap];
//            table = tab;
//
//            // Read the keys and values, and put the mappings in the HashMap
//            for (int i = 0; i < mappings; i++) {
//                @SuppressWarnings("unchecked")
//                    K key = (K) s.readObject();
//                @SuppressWarnings("unchecked")
//                    V value = (V) s.readObject();
//                putVal(hash(key), key, value, false, false);
//            }
//        }
//    }
//
//    /* ------------------------------------------------------------ */
//    // iterators
//
//    abstract class HashIterator {
//        Node<K,V> next;        // next entry to return
//        Node<K,V> current;     // current entry
//        int expectedModCount;  // for fast-fail
//        int index;             // current slot
//
//        HashIterator() {
//            expectedModCount = modCount;
//            Node<K,V>[] t = table;
//            current = next = null;
//            index = 0;
//            if (t != null && size > 0) { // advance to first entry
//                do {} while (index < t.length && (next = t[index++]) == null);
//            }
//        }
//
//        public final boolean hasNext() {
//            return next != null;
//        }
//
//        final Node<K,V> nextNode() {
//            Node<K,V>[] t;
//            Node<K,V> e = next;
//            if (modCount != expectedModCount)
//                throw new ConcurrentModificationException();
//            if (e == null)
//                throw new NoSuchElementException();
//            if ((next = (current = e).next) == null && (t = table) != null) {
//                do {} while (index < t.length && (next = t[index++]) == null);
//            }
//            return e;
//        }
//
//        public final void remove() {
//            Node<K,V> p = current;
//            if (p == null)
//                throw new IllegalStateException();
//            if (modCount != expectedModCount)
//                throw new ConcurrentModificationException();
//            current = null;
//            K key = p.key;
//            removeNode(hash(key), key, null, false, false);
//            expectedModCount = modCount;
//        }
//    }
//
//    final class KeyIterator extends HashIterator
//        implements Iterator<K> {
//        public final K next() { return nextNode().key; }
//    }
//
//    final class ValueIterator extends HashIterator
//        implements Iterator<V> {
//        public final V next() { return nextNode().value; }
//    }
//
//    final class EntryIterator extends HashIterator
//        implements Iterator<Entry<K,V>> {
//        public final Entry<K,V> next() { return nextNode(); }
//    }
//
//    /* ------------------------------------------------------------ */
//    // spliterators
//
//    static class HashMapSpliterator<K,V> {
//        final HashMap<K,V> map;
//        Node<K,V> current;          // current node
//        int index;                  // current index, modified on advance/split
//        int fence;                  // one past last index
//        int est;                    // size estimate
//        int expectedModCount;       // for comodification checks
//
//        HashMapSpliterator(HashMap<K,V> m, int origin,
//                           int fence, int est,
//                           int expectedModCount) {
//            this.map = m;
//            this.index = origin;
//            this.fence = fence;
//            this.est = est;
//            this.expectedModCount = expectedModCount;
//        }
//
//        final int getFence() { // initialize fence and size on first use
//            int hi;
//            if ((hi = fence) < 0) {
//                HashMap<K,V> m = map;
//                est = m.size;
//                expectedModCount = m.modCount;
//                Node<K,V>[] tab = m.table;
//                hi = fence = (tab == null) ? 0 : tab.length;
//            }
//            return hi;
//        }
//
//        public final long estimateSize() {
//            getFence(); // force init
//            return (long) est;
//        }
//    }
//
//    static final class KeySpliterator<K,V>
//        extends HashMapSpliterator<K,V>
//        implements Spliterator<K> {
//        KeySpliterator(HashMap<K,V> m, int origin, int fence, int est,
//                       int expectedModCount) {
//            super(m, origin, fence, est, expectedModCount);
//        }
//
//        public KeySpliterator<K,V> trySplit() {
//            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
//            return (lo >= mid || current != null) ? null :
//                new KeySpliterator<>(map, lo, index = mid, est >>>= 1,
//                                        expectedModCount);
//        }
//
//        public void forEachRemaining(Consumer<? super K> action) {
//            int i, hi, mc;
//            if (action == null)
//                throw new NullPointerException();
//            HashMap<K,V> m = map;
//            Node<K,V>[] tab = m.table;
//            if ((hi = fence) < 0) {
//                mc = expectedModCount = m.modCount;
//                hi = fence = (tab == null) ? 0 : tab.length;
//            }
//            else
//                mc = expectedModCount;
//            if (tab != null && tab.length >= hi &&
//                (i = index) >= 0 && (i < (index = hi) || current != null)) {
//                Node<K,V> p = current;
//                current = null;
//                do {
//                    if (p == null)
//                        p = tab[i++];
//                    else {
//                        action.accept(p.key);
//                        p = p.next;
//                    }
//                } while (p != null || i < hi);
//                if (m.modCount != mc)
//                    throw new ConcurrentModificationException();
//            }
//        }
//
//        public boolean tryAdvance(Consumer<? super K> action) {
//            int hi;
//            if (action == null)
//                throw new NullPointerException();
//            Node<K,V>[] tab = map.table;
//            if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
//                while (current != null || index < hi) {
//                    if (current == null)
//                        current = tab[index++];
//                    else {
//                        K k = current.key;
//                        current = current.next;
//                        action.accept(k);
//                        if (map.modCount != expectedModCount)
//                            throw new ConcurrentModificationException();
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//
//        public int characteristics() {
//            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
//                Spliterator.DISTINCT;
//        }
//    }
//
//    static final class ValueSpliterator<K,V>
//        extends HashMapSpliterator<K,V>
//        implements Spliterator<V> {
//        ValueSpliterator(HashMap<K,V> m, int origin, int fence, int est,
//                         int expectedModCount) {
//            super(m, origin, fence, est, expectedModCount);
//        }
//
//        public ValueSpliterator<K,V> trySplit() {
//            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
//            return (lo >= mid || current != null) ? null :
//                new ValueSpliterator<>(map, lo, index = mid, est >>>= 1,
//                                          expectedModCount);
//        }
//
//        public void forEachRemaining(Consumer<? super V> action) {
//            int i, hi, mc;
//            if (action == null)
//                throw new NullPointerException();
//            HashMap<K,V> m = map;
//            Node<K,V>[] tab = m.table;
//            if ((hi = fence) < 0) {
//                mc = expectedModCount = m.modCount;
//                hi = fence = (tab == null) ? 0 : tab.length;
//            }
//            else
//                mc = expectedModCount;
//            if (tab != null && tab.length >= hi &&
//                (i = index) >= 0 && (i < (index = hi) || current != null)) {
//                Node<K,V> p = current;
//                current = null;
//                do {
//                    if (p == null)
//                        p = tab[i++];
//                    else {
//                        action.accept(p.value);
//                        p = p.next;
//                    }
//                } while (p != null || i < hi);
//                if (m.modCount != mc)
//                    throw new ConcurrentModificationException();
//            }
//        }
//
//        public boolean tryAdvance(Consumer<? super V> action) {
//            int hi;
//            if (action == null)
//                throw new NullPointerException();
//            Node<K,V>[] tab = map.table;
//            if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
//                while (current != null || index < hi) {
//                    if (current == null)
//                        current = tab[index++];
//                    else {
//                        V v = current.value;
//                        current = current.next;
//                        action.accept(v);
//                        if (map.modCount != expectedModCount)
//                            throw new ConcurrentModificationException();
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//
//        public int characteristics() {
//            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0);
//        }
//    }
//
//    static final class EntrySpliterator<K,V>
//        extends HashMapSpliterator<K,V>
//        implements Spliterator<Entry<K,V>> {
//        EntrySpliterator(HashMap<K,V> m, int origin, int fence, int est,
//                         int expectedModCount) {
//            super(m, origin, fence, est, expectedModCount);
//        }
//
//        public EntrySpliterator<K,V> trySplit() {
//            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
//            return (lo >= mid || current != null) ? null :
//                new EntrySpliterator<>(map, lo, index = mid, est >>>= 1,
//                                          expectedModCount);
//        }
//
//        public void forEachRemaining(Consumer<? super Entry<K,V>> action) {
//            int i, hi, mc;
//            if (action == null)
//                throw new NullPointerException();
//            HashMap<K,V> m = map;
//            Node<K,V>[] tab = m.table;
//            if ((hi = fence) < 0) {
//                mc = expectedModCount = m.modCount;
//                hi = fence = (tab == null) ? 0 : tab.length;
//            }
//            else
//                mc = expectedModCount;
//            if (tab != null && tab.length >= hi &&
//                (i = index) >= 0 && (i < (index = hi) || current != null)) {
//                Node<K,V> p = current;
//                current = null;
//                do {
//                    if (p == null)
//                        p = tab[i++];
//                    else {
//                        action.accept(p);
//                        p = p.next;
//                    }
//                } while (p != null || i < hi);
//                if (m.modCount != mc)
//                    throw new ConcurrentModificationException();
//            }
//        }
//
//        public boolean tryAdvance(Consumer<? super Entry<K,V>> action) {
//            int hi;
//            if (action == null)
//                throw new NullPointerException();
//            Node<K,V>[] tab = map.table;
//            if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
//                while (current != null || index < hi) {
//                    if (current == null)
//                        current = tab[index++];
//                    else {
//                        Node<K,V> e = current;
//                        current = current.next;
//                        action.accept(e);
//                        if (map.modCount != expectedModCount)
//                            throw new ConcurrentModificationException();
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//
//        public int characteristics() {
//            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
//                Spliterator.DISTINCT;
//        }
//    }
//
//    /* ------------------------------------------------------------ */
//    // LinkedHashMap support
//
//
//    /*
//     * The following package-protected methods are designed to be
//     * overridden by LinkedHashMap, but not by any other subclass.
//     * Nearly all other internal methods are also package-protected
//     * but are declared final, so can be used by LinkedHashMap, view
//     * classes, and HashSet.
//     */
//
//    // Create a regular (non-tree) node
//    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
//        return new Node<>(hash, key, value, next);
//    }
//
//    // For conversion from TreeNodes to plain nodes
//    Node<K,V> replacementNode(Node<K,V> p, Node<K,V> next) {
//        return new Node<>(p.hash, p.key, p.value, next);
//    }
//
//    // Create a tree bin node
//    TreeNode<K,V> newTreeNode(int hash, K key, V value, Node<K,V> next) {
//        return new TreeNode<>(hash, key, value, next);
//    }
//
//    // For treeifyBin
//    TreeNode<K,V> replacementTreeNode(Node<K,V> p, Node<K,V> next) {
//        return new TreeNode<>(p.hash, p.key, p.value, next);
//    }
//
//    /**
//     * Reset to initial default state.  Called by clone and readObject.
//     */
//    void reinitialize() {
//        table = null;
//        entrySet = null;
//        keySet = null;
//        values = null;
//        modCount = 0;
//        threshold = 0;
//        size = 0;
//    }
//
//    // linkedhashmap拓展使用
//    void afterNodeAccess(Node<K,V> p) { }
//    void afterNodeInsertion(boolean evict) { }
//    void afterNodeRemoval(Node<K,V> p) { }
//
//    // Called only from writeObject, to ensure compatible ordering.
//    void internalWriteEntries(java.io.ObjectOutputStream s) throws IOException {
//        Node<K,V>[] tab;
//        if (size > 0 && (tab = table) != null) {
//            for (int i = 0; i < tab.length; ++i) {
//                for (Node<K,V> e = tab[i]; e != null; e = e.next) {
//                    s.writeObject(e.key);
//                    s.writeObject(e.value);
//                }
//            }
//        }
//    }
//
//    /* ------------------------------------------------------------ */
//    // Tree bins
//
//    /**
//     * Entry for Tree bins. Extends LinkedHashMap.Entry (which in turn
//     * extends Node) so can be used as extension of either regular or
//     * linked node.
//     */
//    static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
//        TreeNode<K,V> parent;  // red-black tree links
//        TreeNode<K,V> left;
//        TreeNode<K,V> right;
//        TreeNode<K,V> prev;    // needed to unlink next upon deletion
//        boolean red;
//        TreeNode(int hash, K key, V val, Node<K,V> next) {
//            super(hash, key, val, next);
//        }
//
//        /**
//         * Returns root of tree containing this node.
//         */
//        final TreeNode<K,V> root() {
//            for (TreeNode<K,V> r = this, p;;) {
//                if ((p = r.parent) == null)
//                    return r;
//                r = p;
//            }
//        }
//
//        /**
//         * Ensures that the given root is the first node of its bin.
//         */
//        static <K,V> void moveRootToFront(Node<K,V>[] tab, TreeNode<K,V> root) {
//            int n;
//            if (root != null && tab != null && (n = tab.length) > 0) {
//                int index = (n - 1) & root.hash;
//                TreeNode<K,V> first = (TreeNode<K,V>)tab[index];
//                if (root != first) {
//                    Node<K,V> rn;
//                    tab[index] = root;
//                    TreeNode<K,V> rp = root.prev;
//                    if ((rn = root.next) != null)
//                        ((TreeNode<K,V>)rn).prev = rp;
//                    if (rp != null)
//                        rp.next = rn;
//                    if (first != null)
//                        first.prev = root;
//                    root.next = first;
//                    root.prev = null;
//                }
//                assert checkInvariants(root);
//            }
//        }
//
//        /**
//         * Finds the node starting at root p with the given hash and key.
//         * The kc argument caches comparableClassFor(key) upon first use
//         * comparing keys.
//         */
//        final TreeNode<K,V> find(int h, Object k, Class<?> kc) {
//            TreeNode<K,V> p = this;
//            do {
//                int ph, dir; K pk;
//                TreeNode<K,V> pl = p.left, pr = p.right, q;
//                if ((ph = p.hash) > h)
//                    p = pl;
//                else if (ph < h)
//                    p = pr;
//                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
//                    return p;
//                else if (pl == null)
//                    p = pr;
//                else if (pr == null)
//                    p = pl;
//                else if ((kc != null ||
//                          (kc = comparableClassFor(k)) != null) &&
//                         (dir = compareComparables(kc, k, pk)) != 0)
//                    p = (dir < 0) ? pl : pr;
//                else if ((q = pr.find(h, k, kc)) != null)
//                    return q;
//                else
//                    p = pl;
//            } while (p != null);
//            return null;
//        }
//
//        /**
//         * Calls find for root node.
//         */
//        final TreeNode<K,V> getTreeNode(int h, Object k) {
//            return ((parent != null) ? root() : this).find(h, k, null);
//        }
//
//        /**
//         * Tie-breaking utility for ordering insertions when equal
//         * hashCodes and non-comparable. We don't require a total
//         * order, just a consistent insertion rule to maintain
//         * equivalence across rebalancings. Tie-breaking further than
//         * necessary simplifies testing a bit.
//         */
//        static int tieBreakOrder(Object a, Object b) {
//            int d;
//            if (a == null || b == null ||
//                (d = a.getClass().getName().
//                 compareTo(b.getClass().getName())) == 0)
//                d = (System.identityHashCode(a) <= System.identityHashCode(b) ?
//                     -1 : 1);
//            return d;
//        }
//
//        /**
//         * Forms tree of the nodes linked from this node.
//         * @return root of tree
//         */
//        final void treeify(Node<K,V>[] tab) {
//            TreeNode<K,V> root = null;
//            for (TreeNode<K,V> x = this, next; x != null; x = next) {
//                next = (TreeNode<K,V>)x.next;
//                x.left = x.right = null;
//                if (root == null) {
//                    x.parent = null;
//                    x.red = false;
//                    root = x;
//                }
//                else {
//                    K k = x.key;
//                    int h = x.hash;
//                    Class<?> kc = null;
//                    for (TreeNode<K,V> p = root;;) {
//                        int dir, ph;
//                        K pk = p.key;
//                        if ((ph = p.hash) > h)
//                            dir = -1;
//                        else if (ph < h)
//                            dir = 1;
//                        else if ((kc == null &&
//                                  (kc = comparableClassFor(k)) == null) ||
//                                 (dir = compareComparables(kc, k, pk)) == 0)
//                            dir = tieBreakOrder(k, pk);
//
//                        TreeNode<K,V> xp = p;
//                        if ((p = (dir <= 0) ? p.left : p.right) == null) {
//                            x.parent = xp;
//                            if (dir <= 0)
//                                xp.left = x;
//                            else
//                                xp.right = x;
//                            root = balanceInsertion(root, x);
//                            break;
//                        }
//                    }
//                }
//            }
//            moveRootToFront(tab, root);
//        }
//
//        /**
//         * Returns a list of non-TreeNodes replacing those linked from
//         * this node.
//         */
//        final Node<K,V> untreeify(HashMap<K,V> map) {
//            Node<K,V> hd = null, tl = null;
//            for (Node<K,V> q = this; q != null; q = q.next) {
//                Node<K,V> p = map.replacementNode(q, null);
//                if (tl == null)
//                    hd = p;
//                else
//                    tl.next = p;
//                tl = p;
//            }
//            return hd;
//        }
//
//        /**
//         * Tree version of putVal.
//         */
//        final TreeNode<K,V> putTreeVal(HashMap<K,V> map, Node<K,V>[] tab,
//                                       int h, K k, V v) {
//            Class<?> kc = null;
//            boolean searched = false;
//            TreeNode<K,V> root = (parent != null) ? root() : this;
//            for (TreeNode<K,V> p = root;;) {
//                int dir, ph; K pk;
//                if ((ph = p.hash) > h)
//                    dir = -1;
//                else if (ph < h)
//                    dir = 1;
//                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
//                    return p;
//                else if ((kc == null &&
//                          (kc = comparableClassFor(k)) == null) ||
//                         (dir = compareComparables(kc, k, pk)) == 0) {
//                    if (!searched) {
//                        TreeNode<K,V> q, ch;
//                        searched = true;
//                        if (((ch = p.left) != null &&
//                             (q = ch.find(h, k, kc)) != null) ||
//                            ((ch = p.right) != null &&
//                             (q = ch.find(h, k, kc)) != null))
//                            return q;
//                    }
//                    dir = tieBreakOrder(k, pk);
//                }
//
//                TreeNode<K,V> xp = p;
//                if ((p = (dir <= 0) ? p.left : p.right) == null) {
//                    Node<K,V> xpn = xp.next;
//                    TreeNode<K,V> x = map.newTreeNode(h, k, v, xpn);
//                    if (dir <= 0)
//                        xp.left = x;
//                    else
//                        xp.right = x;
//                    xp.next = x;
//                    x.parent = x.prev = xp;
//                    if (xpn != null)
//                        ((TreeNode<K,V>)xpn).prev = x;
//                    moveRootToFront(tab, balanceInsertion(root, x));
//                    return null;
//                }
//            }
//        }
//
//        /**
//         * Removes the given node, that must be present before this call.
//         * This is messier than typical red-black deletion code because we
//         * cannot swap the contents of an interior node with a leaf
//         * successor that is pinned by "next" pointers that are accessible
//         * independently during traversal. So instead we swap the tree
//         * linkages. If the current tree appears to have too few nodes,
//         * the bin is converted back to a plain bin. (The test triggers
//         * somewhere between 2 and 6 nodes, depending on tree structure).
//         */
//        final void removeTreeNode(HashMap<K,V> map, Node<K,V>[] tab,
//                                  boolean movable) {
//            int n;
//            if (tab == null || (n = tab.length) == 0)
//                return;
//            int index = (n - 1) & hash;
//            TreeNode<K,V> first = (TreeNode<K,V>)tab[index], root = first, rl;
//            TreeNode<K,V> succ = (TreeNode<K,V>)next, pred = prev;
//            if (pred == null)
//                tab[index] = first = succ;
//            else
//                pred.next = succ;
//            if (succ != null)
//                succ.prev = pred;
//            if (first == null)
//                return;
//            if (root.parent != null)
//                root = root.root();
//            if (root == null || root.right == null ||
//                (rl = root.left) == null || rl.left == null) {
//                tab[index] = first.untreeify(map);  // too small
//                return;
//            }
//            TreeNode<K,V> p = this, pl = left, pr = right, replacement;
//            if (pl != null && pr != null) {
//                TreeNode<K,V> s = pr, sl;
//                while ((sl = s.left) != null) // find successor
//                    s = sl;
//                boolean c = s.red; s.red = p.red; p.red = c; // swap colors
//                TreeNode<K,V> sr = s.right;
//                TreeNode<K,V> pp = p.parent;
//                if (s == pr) { // p was s's direct parent
//                    p.parent = s;
//                    s.right = p;
//                }
//                else {
//                    TreeNode<K,V> sp = s.parent;
//                    if ((p.parent = sp) != null) {
//                        if (s == sp.left)
//                            sp.left = p;
//                        else
//                            sp.right = p;
//                    }
//                    if ((s.right = pr) != null)
//                        pr.parent = s;
//                }
//                p.left = null;
//                if ((p.right = sr) != null)
//                    sr.parent = p;
//                if ((s.left = pl) != null)
//                    pl.parent = s;
//                if ((s.parent = pp) == null)
//                    root = s;
//                else if (p == pp.left)
//                    pp.left = s;
//                else
//                    pp.right = s;
//                if (sr != null)
//                    replacement = sr;
//                else
//                    replacement = p;
//            }
//            else if (pl != null)
//                replacement = pl;
//            else if (pr != null)
//                replacement = pr;
//            else
//                replacement = p;
//            if (replacement != p) {
//                TreeNode<K,V> pp = replacement.parent = p.parent;
//                if (pp == null)
//                    root = replacement;
//                else if (p == pp.left)
//                    pp.left = replacement;
//                else
//                    pp.right = replacement;
//                p.left = p.right = p.parent = null;
//            }
//
//            TreeNode<K,V> r = p.red ? root : balanceDeletion(root, replacement);
//
//            if (replacement == p) {  // detach
//                TreeNode<K,V> pp = p.parent;
//                p.parent = null;
//                if (pp != null) {
//                    if (p == pp.left)
//                        pp.left = null;
//                    else if (p == pp.right)
//                        pp.right = null;
//                }
//            }
//            if (movable)
//                moveRootToFront(tab, r);
//        }
//
//        /**
//         * Splits nodes in a tree bin into lower and upper tree bins,
//         * or untreeifies if now too small. Called only from resize;
//         * see above discussion about split bits and indices.
//         *
//         * @param map the map
//         * @param tab the table for recording bin heads
//         * @param index the index of the table being split
//         * @param bit the bit of hash to split on
//         */
//        final void split(HashMap<K,V> map, Node<K,V>[] tab, int index, int bit) {
//            TreeNode<K,V> b = this;
//            // Relink into lo and hi lists, preserving order
//            TreeNode<K,V> loHead = null, loTail = null;
//            TreeNode<K,V> hiHead = null, hiTail = null;
//            int lc = 0, hc = 0;
//            for (TreeNode<K,V> e = b, next; e != null; e = next) {
//                next = (TreeNode<K,V>)e.next;
//                e.next = null;
//                if ((e.hash & bit) == 0) {
//                    if ((e.prev = loTail) == null)
//                        loHead = e;
//                    else
//                        loTail.next = e;
//                    loTail = e;
//                    ++lc;
//                }
//                else {
//                    if ((e.prev = hiTail) == null)
//                        hiHead = e;
//                    else
//                        hiTail.next = e;
//                    hiTail = e;
//                    ++hc;
//                }
//            }
//
//            if (loHead != null) {
//                if (lc <= UNTREEIFY_THRESHOLD)
//                    tab[index] = loHead.untreeify(map);
//                else {
//                    tab[index] = loHead;
//                    if (hiHead != null) // (else is already treeified)
//                        loHead.treeify(tab);
//                }
//            }
//            if (hiHead != null) {
//                if (hc <= UNTREEIFY_THRESHOLD)
//                    tab[index + bit] = hiHead.untreeify(map);
//                else {
//                    tab[index + bit] = hiHead;
//                    if (loHead != null)
//                        hiHead.treeify(tab);
//                }
//            }
//        }
//
//        /* ------------------------------------------------------------ */
//        // Red-black tree methods, all adapted from CLR
//
//        static <K,V> TreeNode<K,V> rotateLeft(TreeNode<K,V> root,
//                                              TreeNode<K,V> p) {
//            TreeNode<K,V> r, pp, rl;
//            if (p != null && (r = p.right) != null) {
//                if ((rl = p.right = r.left) != null)
//                    rl.parent = p;
//                if ((pp = r.parent = p.parent) == null)
//                    (root = r).red = false;
//                else if (pp.left == p)
//                    pp.left = r;
//                else
//                    pp.right = r;
//                r.left = p;
//                p.parent = r;
//            }
//            return root;
//        }
//
//        static <K,V> TreeNode<K,V> rotateRight(TreeNode<K,V> root,
//                                               TreeNode<K,V> p) {
//            TreeNode<K,V> l, pp, lr;
//            if (p != null && (l = p.left) != null) {
//                if ((lr = p.left = l.right) != null)
//                    lr.parent = p;
//                if ((pp = l.parent = p.parent) == null)
//                    (root = l).red = false;
//                else if (pp.right == p)
//                    pp.right = l;
//                else
//                    pp.left = l;
//                l.right = p;
//                p.parent = l;
//            }
//            return root;
//        }
//
//        static <K,V> TreeNode<K,V> balanceInsertion(TreeNode<K,V> root,
//                                                    TreeNode<K,V> x) {
//            x.red = true;
//            for (TreeNode<K,V> xp, xpp, xppl, xppr;;) {
//                if ((xp = x.parent) == null) {
//                    x.red = false;
//                    return x;
//                }
//                else if (!xp.red || (xpp = xp.parent) == null)
//                    return root;
//                if (xp == (xppl = xpp.left)) {
//                    if ((xppr = xpp.right) != null && xppr.red) {
//                        xppr.red = false;
//                        xp.red = false;
//                        xpp.red = true;
//                        x = xpp;
//                    }
//                    else {
//                        if (x == xp.right) {
//                            root = rotateLeft(root, x = xp);
//                            xpp = (xp = x.parent) == null ? null : xp.parent;
//                        }
//                        if (xp != null) {
//                            xp.red = false;
//                            if (xpp != null) {
//                                xpp.red = true;
//                                root = rotateRight(root, xpp);
//                            }
//                        }
//                    }
//                }
//                else {
//                    if (xppl != null && xppl.red) {
//                        xppl.red = false;
//                        xp.red = false;
//                        xpp.red = true;
//                        x = xpp;
//                    }
//                    else {
//                        if (x == xp.left) {
//                            root = rotateRight(root, x = xp);
//                            xpp = (xp = x.parent) == null ? null : xp.parent;
//                        }
//                        if (xp != null) {
//                            xp.red = false;
//                            if (xpp != null) {
//                                xpp.red = true;
//                                root = rotateLeft(root, xpp);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        static <K,V> TreeNode<K,V> balanceDeletion(TreeNode<K,V> root,
//                                                   TreeNode<K,V> x) {
//            for (TreeNode<K,V> xp, xpl, xpr;;)  {
//                if (x == null || x == root)
//                    return root;
//                else if ((xp = x.parent) == null) {
//                    x.red = false;
//                    return x;
//                }
//                else if (x.red) {
//                    x.red = false;
//                    return root;
//                }
//                else if ((xpl = xp.left) == x) {
//                    if ((xpr = xp.right) != null && xpr.red) {
//                        xpr.red = false;
//                        xp.red = true;
//                        root = rotateLeft(root, xp);
//                        xpr = (xp = x.parent) == null ? null : xp.right;
//                    }
//                    if (xpr == null)
//                        x = xp;
//                    else {
//                        TreeNode<K,V> sl = xpr.left, sr = xpr.right;
//                        if ((sr == null || !sr.red) &&
//                            (sl == null || !sl.red)) {
//                            xpr.red = true;
//                            x = xp;
//                        }
//                        else {
//                            if (sr == null || !sr.red) {
//                                if (sl != null)
//                                    sl.red = false;
//                                xpr.red = true;
//                                root = rotateRight(root, xpr);
//                                xpr = (xp = x.parent) == null ?
//                                    null : xp.right;
//                            }
//                            if (xpr != null) {
//                                xpr.red = (xp == null) ? false : xp.red;
//                                if ((sr = xpr.right) != null)
//                                    sr.red = false;
//                            }
//                            if (xp != null) {
//                                xp.red = false;
//                                root = rotateLeft(root, xp);
//                            }
//                            x = root;
//                        }
//                    }
//                }
//                else { // symmetric
//                    if (xpl != null && xpl.red) {
//                        xpl.red = false;
//                        xp.red = true;
//                        root = rotateRight(root, xp);
//                        xpl = (xp = x.parent) == null ? null : xp.left;
//                    }
//                    if (xpl == null)
//                        x = xp;
//                    else {
//                        TreeNode<K,V> sl = xpl.left, sr = xpl.right;
//                        if ((sl == null || !sl.red) &&
//                            (sr == null || !sr.red)) {
//                            xpl.red = true;
//                            x = xp;
//                        }
//                        else {
//                            if (sl == null || !sl.red) {
//                                if (sr != null)
//                                    sr.red = false;
//                                xpl.red = true;
//                                root = rotateLeft(root, xpl);
//                                xpl = (xp = x.parent) == null ?
//                                    null : xp.left;
//                            }
//                            if (xpl != null) {
//                                xpl.red = (xp == null) ? false : xp.red;
//                                if ((sl = xpl.left) != null)
//                                    sl.red = false;
//                            }
//                            if (xp != null) {
//                                xp.red = false;
//                                root = rotateRight(root, xp);
//                            }
//                            x = root;
//                        }
//                    }
//                }
//            }
//        }
//
//        /**
//         * Recursive invariant check
//         */
//        static <K,V> boolean checkInvariants(TreeNode<K,V> t) {
//            TreeNode<K,V> tp = t.parent, tl = t.left, tr = t.right,
//                tb = t.prev, tn = (TreeNode<K,V>)t.next;
//            if (tb != null && tb.next != t)
//                return false;
//            if (tn != null && tn.prev != t)
//                return false;
//            if (tp != null && t != tp.left && t != tp.right)
//                return false;
//            if (tl != null && (tl.parent != t || tl.hash > t.hash))
//                return false;
//            if (tr != null && (tr.parent != t || tr.hash < t.hash))
//                return false;
//            if (t.red && tl != null && tl.red && tr != null && tr.red)
//                return false;
//            if (tl != null && !checkInvariants(tl))
//                return false;
//            if (tr != null && !checkInvariants(tr))
//                return false;
//            return true;
//        }
//    }
//
//}
