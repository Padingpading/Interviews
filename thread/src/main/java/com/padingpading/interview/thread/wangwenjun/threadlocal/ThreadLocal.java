/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.padingpading.interview.thread.wangwenjun.threadlocal;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ThreadLocal<T> {
    /**当前ThreadLocal对象的hashcode
     * 常量,对象创建的时候生成,调用nextHashCode()
     * nextHashCode()=自增的i+偏移量   所以每一个threadlcoal的hashcode不一致。
     */
    private final int threadLocalHashCode = nextHashCode();

    /**原子计数器
     * The next hash code to be given out. Updated atomically. Starts at
     * zero.
     */
    private static AtomicInteger nextHashCode =
        new AtomicInteger();

    /**
     * The difference between successively generated hash codes - turns
     * implicit sequential thread-local IDs into near-optimally spread
     * multiplicative hash values for power-of-two-sized tables.
     */
    private static final int HASH_INCREMENT = 0x61c88647;

    /**
     * Returns the next hash code.
     */
    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    /**初始化值。
     */
    protected T initialValue() {
        return null;
    }

    /**提供一个初始化值得supplier
     */
    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
        return new SuppliedThreadLocal<>(supplier);
    }

    /**无参构造
     */
    public ThreadLocal() {
    }

    /**Threadlocal
     * 当前线程的value。
     */
    public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }

    /**设置当前线程的初始化值。
     */
    private T setInitialValue() {
        T value = initialValue();
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
        return value;
    }

    /**设置值。
     */
    public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            //存在设置值。
            map.set(this, value);
        else
            //不存在,直接创建
            createMap(t, value);
    }

    /**移除ThreadLocalMap
     */
     public void remove() {
         ThreadLocalMap m = getMap(Thread.currentThread());
         if (m != null)
             m.remove(this);
     }

    /**获取threadLocalMaps。
     */
    ThreadLocalMap getMap(Thread t) {
        return t.threadLocals;
    }

    /**创建map并且附带初始值。
     */
    void createMap(Thread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this, firstValue);
    }

    /**创建ThreadLocalMap
     */
    static ThreadLocalMap createInheritedMap(ThreadLocalMap parentMap) {
        return new ThreadLocalMap(parentMap);
    }

    /**
     * Method childValue is visibly defined in subclass
     * InheritableThreadLocal, but is internally defined here for the
     * sake of providing createInheritedMap factory method without
     * needing to subclass the map class in InheritableThreadLocal.
     * This technique is preferable to the alternative of embedding
     * instanceof tests in methods.
     */
    T childValue(T parentValue) {
        throw new UnsupportedOperationException();
    }

    /**ThreadLocal子类,重写获取初始化值,全局的是初始化值。
     *
     */
    static final class SuppliedThreadLocal<T> extends ThreadLocal<T> {

        private final Supplier<? extends T> supplier;

        SuppliedThreadLocal(Supplier<? extends T> supplier) {
            this.supplier = Objects.requireNonNull(supplier);
        }

        @Override
        protected T initialValue() {
            return supplier.get();
        }
    }

    /**
     * ThreadLocalMap is a customized hash map suitable only for
     * maintaining thread local values. No operations are exported
     * outside of the ThreadLocal class. The class is package private to
     * allow declaration of fields in class Thread.  To help deal with
     * very large and long-lived usages, the hash table entries use
     * WeakReferences for keys. However, since reference queues are not
     * used, stale entries are guaranteed to be removed only when
     * the table starts running out of space.
     */
    static class ThreadLocalMap {

        /**弱引用的entry
         */
        static class Entry extends WeakReference<ThreadLocal<?>> {
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }

        /**
         * The initial capacity -- MUST be a power of two.
         */
        private static final int INITIAL_CAPACITY = 16;

        /**
         * The table, resized as necessary.
         * table.length MUST always be a power of two.
         */
        private Entry[] table;

        /**
         * The number of entries in the table.
         */
        private int size = 0;

        /**数组扩容阈值
         * The next size value at which to resize.
         */
        private int threshold; // Default to 0

        /**
         * Set the resize threshold to maintain at worst a 2/3 load factor.
         */
        private void setThreshold(int len) {
            threshold = len * 2 / 3;
        }

        /**向后递增,超过最大值,跳到0位置,i >= len ,返回0
         * Increment i modulo len.
         */
        private static int nextIndex(int i, int len) {
            return ((i + 1 < len) ? i + 1 : 0);
        }

        /**
         * Decrement i modulo len.
         */
        private static int prevIndex(int i, int len) {
            return ((i - 1 >= 0) ? i - 1 : len - 1);
        }

        /**
         * Construct a new map initially containing (firstKey, firstValue).
         * ThreadLocalMaps are constructed lazily, so we only create
         * one when we have at least one entry to put in it.
         */
        ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
            //默认对数组初始化值16
            table = new Entry[INITIAL_CAPACITY];
            //firstKey.threadLocalHashCode:当前threadlocal对象的hashcode
            int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
            table[i] = new Entry(firstKey, firstValue);
            size = 1;
            //len * 2 / 3
            setThreshold(INITIAL_CAPACITY);
        }

        /**
         * Construct a new map including all Inheritable ThreadLocals
         * from given parent map. Called only by createInheritedMap.
         *
         * @param parentMap the map associated with parent thread.
         */
        private ThreadLocalMap(ThreadLocalMap parentMap) {
            Entry[] parentTable = parentMap.table;
            int len = parentTable.length;
            setThreshold(len);
            table = new Entry[len];

            for (int j = 0; j < len; j++) {
                Entry e = parentTable[j];
                if (e != null) {
                    @SuppressWarnings("unchecked")
                    ThreadLocal<Object> key = (ThreadLocal<Object>) e.get();
                    if (key != null) {
                        Object value = key.childValue(e.value);
                        Entry c = new Entry(key, value);
                        int h = key.threadLocalHashCode & (len - 1);
                        while (table[h] != null)
                            h = nextIndex(h, len);
                        table[h] = c;
                        size++;
                    }
                }
            }
        }

        /**
         * Get the entry associated with key.  This method
         * itself handles only the fast path: a direct hit of existing
         * key. It otherwise relays to getEntryAfterMiss.  This is
         * designed to maximize performance for direct hits, in part
         * by making this method readily inlinable.
         *
         * @param  key the thread local object
         * @return the entry associated with key, or null if no such
         */
        private Entry getEntry(ThreadLocal<?> key) {
            int i = key.threadLocalHashCode & (table.length - 1);
            Entry e = table[i];
            if (e != null && e.get() == key)
                return e;
            else
                return getEntryAfterMiss(key, i, e);
        }

        /**
         * Version of getEntry method for use when key is not found in
         * its direct hash slot.
         *
         * @param  key the thread local object
         * @param  i the table index for key's hash code
         * @param  e the entry at table[i]
         * @return the entry associated with key, or null if no such
         */
        private Entry getEntryAfterMiss(ThreadLocal<?> key, int i, Entry e) {
            Entry[] tab = table;
            int len = tab.length;

            while (e != null) {
                ThreadLocal<?> k = e.get();
                if (k == key)
                    return e;
                if (k == null)
                    expungeStaleEntry(i);
                else
                    i = nextIndex(i, len);
                e = tab[i];
            }
            return null;
        }

        /**设置entry的值
         */
        private void set(ThreadLocal<?> key, Object value) {

            Entry[] tab = table;
            int len = tab.length;
            //index位置
            int i = key.threadLocalHashCode & (len-1);
            //entry部位null,向后遍历元素
            for (Entry e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {
                //遍历数组
                ThreadLocal<?> k = e.get();
                //比较key相等,相等直接覆盖。
                if (k == key) {
                    e.value = value;
                    return;
                }
                //entry不为null，说明当前桶位置的Entry是过期数据
                if (k == null) {
                    //过期数据,
                    replaceStaleEntry(key, value, i);
                    return;
                }
            }
            //遇到entry为null的元素
            //创建数组对象
            tab[i] = new Entry(key, value);
            //数量+1
            int sz = ++size;
            //调用cleanSomeSlots()做一次启发式清理工作，清理散列数组中Entry的key过期的数据
            if (!cleanSomeSlots(i, sz) && sz >= threshold)
                //如果清理工作完成后，未清理到任何数据，且size超过了阈值(数组长度的2/3)，进行rehash()
                rehash();
        }

        /**
         * Remove the entry for key.
         */
        private void remove(ThreadLocal<?> key) {
            Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len-1);
            for (Entry e = tab[i];
                 e != null;
                 e = tab[i = nextIndex(i, len)]) {
                if (e.get() == key) {
                    e.clear();
                    expungeStaleEntry(i);
                    return;
                }
            }
        }

        /**
         * Replace a stale entry encountered during a set operation
         * with an entry for the specified key.  The value passed in
         * the value parameter is stored in the entry, whether or not
         * an entry already exists for the specified key.
         *
         * As a side effect, this method expunges all stale entries in the
         * "run" containing the stale entry.  (A run is a sequence of entries
         * between two null slots.)
         *
         * @param  key the key
         * @param  value the value to be associated with key
         * @param  staleSlot index of the first stale entry encountered while
         *         searching for key.
         */
        private void replaceStaleEntry(ThreadLocal<?> key, Object value,
                                       int staleSlot) {
            Entry[] tab = table;
            int len = tab.length;
            Entry e;
            //开始探测的起始位置staleSlot(元素过期的位置)
            int slotToExpunge = staleSlot;
            //向前探测,探测到entry==null为结束条件
            for (int i = prevIndex(staleSlot, len); (e = tab[i]) != null; i = prevIndex(i, len))
                if (e.get() == null)
                    //更新的过去元素的位置。
                    slotToExpunge = i;
            //向后探测 结束条件为 entry==null
            for (int i = nextIndex(staleSlot, len); (e = tab[i]) != null; i = nextIndex(i, len)) {
                ThreadLocal<?> k = e.get();
                //添加的key和当前entry的key相等
                if (k == key) {
                    //更新值。
                    e.value = value;
                    //和过期元素进行替换。
                    tab[i] = tab[staleSlot];
                    tab[staleSlot] = e;
                    //如果staleSlot位置前面没有过期元素
                    if (slotToExpunge == staleSlot)
                        //开始探测式清理过期数据的下标为当前循环的index
                        slotToExpunge = i;
                    //进行启发式过期数据清理
                    //一个是过期key相关Entry的启发式清理(Heuristically scan)
                    //另一个是过期key相关Entry的探测式清理。
                    cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
                    return;
                }
                //过期数据，
                if (k == null && slotToExpunge == staleSlot)
                    //一开始的向前查找数据并未找到过期的Entry，。如果条件成立，则更新slotToExpunge 为当前位置，这个前提是前驱节点扫描时未发现过期数据。
                    slotToExpunge = i;
            }
            //找到 entry为null的位置
            tab[staleSlot].value = null;
            //创建新的元素
            tab[staleSlot] = new Entry(key, value);

            // 最后判断除了staleSlot以外，还发现了其他过期的slot数据，就要开启清理数据的逻辑：
            if (slotToExpunge != staleSlot)
                cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
        }

        /**探测式清理
         */
        private int expungeStaleEntry(int staleSlot) {
            //staleSlot 位置开始为过期远古三。
            Entry[] tab = table;
            int len = tab.length;

            //设置过期元素为null
            tab[staleSlot].value = null;
            tab[staleSlot] = null;
            size--;

            Entry e;
            int i;
            //向后遍历
            for (i = nextIndex(staleSlot, len); (e = tab[i]) != null; i = nextIndex(i, len)) {
                ThreadLocal<?> k = e.get();
                //过期元素的entry
                if (k == null) {
                    //清除entry
                    e.value = null;
                    tab[i] = null;
                    size--;
                } else {
                    //rehash。
                    int h = k.threadLocalHashCode & (len - 1);
                    if (h != i) {
                        //计算后的位置不是当前的位置,说明前面有空缺。
                        tab[i] = null;
                        //获取到对应的数组位置,赋值。
                        while (tab[h] != null)
                            h = nextIndex(h, len);
                        tab[h] = e;
                    }
                }
            }
            return i;
        }

        /**
         * Heuristically scan some cells looking for stale entries.
         * This is invoked when either a new element is added, or
         * another stale one has been expunged. It performs a
         * logarithmic number of scans, as a balance between no
         * scanning (fast but retains garbage) and a number of scans
         * proportional to number of elements, that would find all
         * garbage but would cause some insertions to take O(n) time.
         *
         * @param i a position known NOT to hold a stale entry. The
         * scan starts at the element after i.
         *
         * @param n scan control: {@code log2(n)} cells are scanned,
         * unless a stale entry is found, in which case
         * {@code log2(table.length)-1} additional cells are scanned.
         * When called from insertions, this parameter is the number
         * of elements, but when from replaceStaleEntry, it is the
         * table length. (Note: all this could be changed to be either
         * more or less aggressive by weighting n instead of just
         * using straight log n. But this version is simple, fast, and
         * seems to work well.)
         *
         * @return true if any stale entries have been removed.
         */
        private boolean cleanSomeSlots(int i, int n) {
            boolean removed = false;
            Entry[] tab = table;
            int len = tab.length;
            do {
                i = nextIndex(i, len);
                Entry e = tab[i];
                if (e != null && e.get() == null) {
                    n = len;
                    removed = true;
                    i = expungeStaleEntry(i);
                }
            } while ( (n >>>= 1) != 0);
            return removed;
        }

        /**扩容
         */
        private void rehash() {
            //rehash()中会先进行一轮探测式清理，清理过期key，清理完成后如果size >= threshold - threshold / 4，就会执行真正的扩容逻辑(扩容逻辑往后看)
            expungeStaleEntries();

            // Use lower threshold for doubling to avoid hysteresis
            if (size >= threshold - threshold / 4)
                resize();
        }

        /**
         * Double the capacity of the table.
         */
        private void resize() {
            Entry[] oldTab = table;
            int oldLen = oldTab.length;
            int newLen = oldLen * 2;
            Entry[] newTab = new Entry[newLen];
            int count = 0;

            for (int j = 0; j < oldLen; ++j) {
                Entry e = oldTab[j];
                if (e != null) {
                    ThreadLocal<?> k = e.get();
                    if (k == null) {
                        e.value = null; // Help the GC
                    } else {
                        int h = k.threadLocalHashCode & (newLen - 1);
                        while (newTab[h] != null)
                            h = nextIndex(h, newLen);
                        newTab[h] = e;
                        count++;
                    }
                }
            }

            setThreshold(newLen);
            size = count;
            table = newTab;
        }

        /**
         * Expunge all stale entries in the table.
         */
        private void expungeStaleEntries() {
            Entry[] tab = table;
            int len = tab.length;
            for (int j = 0; j < len; j++) {
                Entry e = tab[j];
                if (e != null && e.get() == null)
                    expungeStaleEntry(j);
            }
        }
    }
}
