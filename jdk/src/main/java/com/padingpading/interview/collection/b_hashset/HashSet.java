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
//package com.padingpading.interview.collection.b_hashset;
//
//import sun.misc.SharedSecrets;
//
//import java.io.InvalidObjectException;
//import java.util.AbstractSet;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.ConcurrentModificationException;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.LinkedHashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.Spliterator;
//import java.util.TreeSet;
//
///**
// * AbstractSet:实现equals和hashcode方法。
// * Set:无序,不可以重复。
// * Cloneable
// * Serializable:自定义序列化方式。
// */
//public class HashSet<E>
//    extends AbstractSet<E>
//    implements Set<E>, Cloneable, java.io.Serializable
//{
//    static final long serialVersionUID = -5024744406713321676L;
//    // HashSet底层map,不可进行序列化
//    private transient HashMap<E,Object> map;
//
//    // 虚拟对象
//    private static final Object PRESENT = new Object();
//
//    /*==================================================构造方法========================================================*/
//    /** 默认无参构造
//     * 初始化hashmap:初始容量16,加载因子0.75
//     */
//    public HashSet() {
//        map = new HashMap<>();
//    }
//
//    /**根据已有集合元素来构造HashSet
//     */
//    public HashSet(Collection<? extends E> c) {
//        //初始化容量 size/0.75 + 1
//        map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
//        addAll(c);
//    }
//
//    /**给定初始容量和加载因子
//     */
//    public HashSet(int initialCapacity, float loadFactor) {
//        map = new HashMap<>(initialCapacity, loadFactor);
//    }
//
//    /**给定初始容量
//     */
//    public HashSet(int initialCapacity) {
//        map = new HashMap<>(initialCapacity);
//    }
//
//    /**这个构造函数外部不能调用，供LinkedHashSet复写
//     */
//    HashSet(int initialCapacity, float loadFactor, boolean dummy) {
//        //创建 new LinkedHashMap调用
//        map = new LinkedHashMap<>(initialCapacity, loadFactor);
//    }
//
//    /**集合元素数量
//     */
//    public int size() {
//        return map.size();
//    }
//
//
//    /**集合元素是否为空
//     */
//    public boolean isEmpty() {
//        return map.isEmpty();
//    }
//
//    /**添加元素
//     * map的value指向同一个对。
//     */
//    public boolean add(E e) {
//        return map.put(e, PRESENT)==null;
//    }
//
//    /**移除元素。
//     */
//    public boolean remove(Object o) {
//        return map.remove(o)==PRESENT;
//    }
//
//    /**清除元素
//     */
//    public void clear() {
//        map.clear();
//    }
//
//    /**是否包含元素。
//     */
//    public boolean contains(Object o) {
//        return map.containsKey(o);
//    }
//
//
//    /**KetSet迭代器
//     */
//    public Iterator<E> iterator() {
//        return map.keySet().iterator();
//    }
//
//    /**
//     */
//    public Object clone() {
//        try {
//            HashSet<E> newSet = (HashSet<E>) super.clone();
//            newSet.map = (HashMap<E, Object>) map.clone();
//            return newSet;
//        } catch (CloneNotSupportedException e) {
//            throw new InternalError(e);
//        }
//    }
//
//    /*学历恶化
//     */
//    private void writeObject(java.io.ObjectOutputStream s)
//        throws java.io.IOException {
//        // Write out any hidden serialization magic
//        s.defaultWriteObject();
//
//        // Write out HashMap capacity and load factor
//        s.writeInt(map.capacity());
//        s.writeFloat(map.loadFactor());
//
//        // Write out size
//        s.writeInt(map.size());
//
//        // Write out all elements in the proper order.
//        for (E e : map.keySet())
//            s.writeObject(e);
//    }
//
//    /**
//     * Reconstitute the <tt>HashSet</tt> instance from a stream (that is,
//     * deserialize it).
//     */
//    private void readObject(java.io.ObjectInputStream s)
//        throws java.io.IOException, ClassNotFoundException {
//        // Read in any hidden serialization magic
//        s.defaultReadObject();
//
//        // Read capacity and verify non-negative.
//        int capacity = s.readInt();
//        if (capacity < 0) {
//            throw new InvalidObjectException("Illegal capacity: " +
//                                             capacity);
//        }
//
//        // Read load factor and verify positive and non NaN.
//        float loadFactor = s.readFloat();
//        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
//            throw new InvalidObjectException("Illegal load factor: " +
//                                             loadFactor);
//        }
//
//        // Read size and verify non-negative.
//        int size = s.readInt();
//        if (size < 0) {
//            throw new InvalidObjectException("Illegal size: " +
//                                             size);
//        }
//        // Set the capacity according to the size and load factor ensuring that
//        // the HashMap is at least 25% full but clamping to maximum capacity.
//        capacity = (int) Math.min(size * Math.min(1 / loadFactor, 4.0f),
//                HashMap.MAXIMUM_CAPACITY);
//
//        // Constructing the backing map will lazily create an array when the first element is
//        // added, so check it before construction. Call HashMap.tableSizeFor to compute the
//        // actual allocation size. Check Map.Entry[].class since it's the nearest public type to
//        // what is actually created.
//
//        SharedSecrets.getJavaOISAccess()
//                     .checkArray(s, Map.Entry[].class, HashMap.tableSizeFor(capacity));
//
//        // Create backing HashMap
//        map = (((HashSet<?>)this) instanceof LinkedHashSet ?
//               new LinkedHashMap<E,Object>(capacity, loadFactor) :
//               new HashMap<E,Object>(capacity, loadFactor));
//
//        // Read in all elements in the proper order.
//        for (int i=0; i<size; i++) {
//            @SuppressWarnings("unchecked")
//                E e = (E) s.readObject();
//            map.put(e, PRESENT);
//        }
//    }
//
//    /**
//     * Creates a <em><a href="Spliterator.html#binding">late-binding</a></em>
//     * and <em>fail-fast</em> {@link Spliterator} over the elements in this
//     * set.
//     *
//     * <p>The {@code Spliterator} reports {@link Spliterator#SIZED} and
//     * {@link Spliterator#DISTINCT}.  Overriding implementations should document
//     * the reporting of additional characteristic values.
//     *
//     * @return a {@code Spliterator} over the elements in this set
//     * @since 1.8
//     */
//    public Spliterator<E> spliterator() {
//        return new HashMap.KeySpliterator<E,Object>(map, 0, -1, 0, 0);
//    }
//}