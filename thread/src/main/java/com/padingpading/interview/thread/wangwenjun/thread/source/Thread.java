///*
// * Copyright (c) 1994, 2016, Oracle and/or its affiliates. All rights reserved.
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
//package com.padingpading.interview.thread.wangwenjun.thread.source;
//
//import sun.nio.ch.Interruptible;
//import sun.reflect.CallerSensitive;
//import sun.reflect.Reflection;
//import sun.security.util.SecurityConstants;
//
//import java.lang.ref.Reference;
//import java.lang.ref.ReferenceQueue;
//import java.lang.ref.WeakReference;
//import java.security.AccessControlContext;
//import java.security.AccessController;
//import java.security.PrivilegedAction;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.locks.LockSupport;
//
///**Thread
// * 实现接口Runnable,重写run方法。
// */
//public class Thread implements Runnable {
//
//    //定义registerNatives()本地方法注册系统资源
//    private static native void registerNatives();
//
//    static {
//        //在静态代码块中调用注册本地系统资源的方法
//        registerNatives();
//    }
//
//    private volatile String name;
//    private int            priority;
//    private Thread         threadQ;
//    private long           eetop;
//
//    /* Whether or not to single_step this thread. */
//    private boolean     single_step;
//
//    /* Whether or not the thread is a daemon thread. */
//    private boolean     daemon = false;
//
//    /* JVM state */
//    private boolean     stillborn = false;
//
//    /* What will be run. */
//    private Runnable target;
//
//    /* The group of this thread */
//    private ThreadGroup group;
//
//    /**上下文Classloader
//     * 便于 上层类加载器加载的核心类 能够获取到该classloader,进行队列的加载。
//     * 虽然能够能够通过Class.getClassLoader获取到类加载,但是对下层类是不可见的。
//     */
//    private ClassLoader contextClassLoader;
//
//    /* The inherited AccessControlContext of this thread */
//    private AccessControlContext inheritedAccessControlContext;
//
//    /* For autonumbering anonymous threads. */
//    private static int threadInitNumber;
//    private static synchronized int nextThreadNum() {
//        return threadInitNumber++;
//    }
//
//    /**
//     * 当前线程的Threadlocal
//     */
//    ThreadLocal.ThreadLocalMap threadLocals = null;
//
//
//    /**
//     * 父线程的threadlocal
//     */
//    ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;
//
//    /*当前线程jvm栈的深度
//     */
//    private long stackSize;
//
//    /*
//     * JVM-private state that persists after native thread termination.
//     */
//    private long nativeParkEventPointer;
//
//    /*
//     * Thread ID
//     */
//    private long tid;
//
//    /**线程的序号(静态变量)
//     * 在不指定线程名称下,线程的名称为 Thread+threadSeqNumber
//     */
//    private static long threadSeqNumber;
//
//    /* Java thread status for tools,
//     * initialized to indicate thread 'not yet started'
//     */
//
//    private volatile int threadStatus = 0;
//
//
//    /**threadSeqNumber++ 同步方法
//     */
//    private static synchronized long nextThreadID() {
//        return ++threadSeqNumber;
//    }
//
//    /**
//     * The argument supplied to the current call to
//     * java.util.concurrent.locks.LockSupport.park.
//     * Set by (private) java.util.concurrent.locks.LockSupport.setBlocker
//     * Accessed using java.util.concurrent.locks.LockSupport.getBlocker
//     */
//    volatile Object parkBlocker;
//
//    /* The object in which this thread is blocked in an interruptible I/O
//     * operation, if any.  The blocker's interrupt method should be invoked
//     * after setting this thread's interrupt status.
//     */
//    private volatile Interruptible blocker;
//    private final Object blockerLock = new Object();
//
//    /* Set the blocker field; invoked via sun.misc.SharedSecrets from java.nio code
//     */
//    void blockedOn(Interruptible b) {
//        synchronized (blockerLock) {
//            blocker = b;
//        }
//    }
//
//    /**
//     */
//    public final static int MIN_PRIORITY = 1;
//
//   /**
//     */
//    public final static int NORM_PRIORITY = 5;
//
//    /**
//     */
//    public final static int MAX_PRIORITY = 10;
//
//    /**
//     */
//    public static native Thread currentThread();
//
//
//
//    /*==================================================构造函数=====================================================*/
//    /**
//     * 空构造
//     */
//    public Thread() {
//        init(null, null, "Thread-" + nextThreadNum(), 0);
//    }
//
//    /**
//     * 添加执行任务
//     */
//    public Thread(Runnable target) {
//        init(null, target, "Thread-" + nextThreadNum(), 0);
//    }
//
//    /**
//     * Creates a new Thread that inherits the given AccessControlContext.
//     * This is not a public constructor.
//     */
//    Thread(Runnable target, AccessControlContext acc) {
//        init(null, target, "Thread-" + nextThreadNum(), 0, acc, false);
//    }
//
//    /**初始化线程
//     * 线程组
//     * 执行任务
//     */
//    public Thread(ThreadGroup group, Runnable target) {
//        init(group, target, "Thread-" + nextThreadNum(), 0);
//    }
//
//    /**初始化线程名字
//     */
//    public Thread(String name) {
//        init(null, null, name, 0);
//    }
//
//    /**指定线程组,线程的名称。
//     */
//    public Thread(ThreadGroup group, String name) {
//        init(group, null, name, 0);
//    }
//
//    /**初始化线程
//     * target:执行任务。
//     * name:线程名称
//     */
//    public Thread(Runnable target, String name) {
//        init(null, target, name, 0);
//    }
//
//    /**初始化线程
//     * group:线程组
//     * target:执行任务
//     * name:线程名称
//     */
//    public Thread(ThreadGroup group, Runnable target, String name) {
//        init(group, target, name, 0);
//    }
//
//    /**分配一个新的 Thread对象，以便它具有 target作为其运行对象，
//     * 将指定的 name正如其名，以及属于该线程组由称作 group ，并具有指定的 堆栈大小。
//     */
//    public Thread(ThreadGroup group, Runnable target, String name,
//            long stackSize) {
//        init(group, target, name, stackSize);
//    }
//
//
//    /**
//     */
//    private void init(ThreadGroup g, Runnable target, String name,
//                      long stackSize) {
//        init(g, target, name, stackSize, null, true);
//    }
//
//    /**
//     */
//    private void init(ThreadGroup g, Runnable target, String name,
//                      long stackSize, AccessControlContext acc,
//                      boolean inheritThreadLocals) {
//        //线程的名称
//        if (name == null) {
//            throw new NullPointerException("name cannot be null");
//        }
//        this.name = name;
//
//        //父线程
//        Thread parent = currentThread();
//        //获取系统安全管理器
//        SecurityManager security = System.getSecurityManager();
//        //线程组为空
//        if (g == null) {
//
//            //获取的系统安全管理器不为空
//            if (security != null) {
//                //从系统安全管理器中获取一个线程分组
//                g = security.getThreadGroup();
//            }
//            //线程分组为空，则从父线程获取
//            if (g == null) {
//                g = parent.getThreadGroup();
//            }
//        }
//
//        //检查线程组的访问权限
//        g.checkAccess();
//
//        //检查权限
//        if (security != null) {
//            if (isCCLOverridden(getClass())) {
//                security.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
//            }
//        }
//
//        g.addUnstarted();
//
//        //当前线程继承父线程的相关属性
//        //线程组
//        this.group = g;
//        //线程组是否是守护线程
//        this.daemon = parent.isDaemon();
//        //父线程的优先级
//        this.priority = parent.getPriority();
//        //线程的Classloader,获取父线程ClassLoader,父线程为main线程
//        //main线程的类是应用列加载器加载,所以获取到父线程的加载器为应用类加载器。
//        if (security == null || isCCLOverridden(parent.getClass()))
//            this.contextClassLoader = parent.getContextClassLoader();
//        else
//            this.contextClassLoader = parent.contextClassLoader;
//
//        this.inheritedAccessControlContext =
//                acc != null ? acc : AccessController.getContext();
//        //Runable执行任务对象
//        this.target = target;
//        //设置线程优先级
//        setPriority(priority);
//
//        //todo Threadlocal?
//        if (inheritThreadLocals && parent.inheritableThreadLocals != null)
//            this.inheritableThreadLocals =
//                ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
//
//        //jvm栈的深度。
//        this.stackSize = stackSize;
//
//        //线程id
//        tid = nextThreadID();
//    }
//
//
//    /**强制释放cpu执行权,不会释放锁
//     */
//    public static native void yield();
//
//    /**sleep
//     */
//    public static native void sleep(long millis) throws InterruptedException;
//
//    /**sleep
//     */
//    public static void sleep(long millis, int nanos)
//            throws InterruptedException {
//        if (millis < 0) {
//            throw new IllegalArgumentException("timeout value is negative");
//        }
//
//        if (nanos < 0 || nanos > 999999) {
//            throw new IllegalArgumentException(
//                    "nanosecond timeout value out of range");
//        }
//
//        if (nanos >= 500000 || (nanos != 0 && millis == 0)) {
//            millis++;
//        }
//
//        sleep(millis);
//    }
//
//    /**
//     * Throws CloneNotSupportedException as a Thread can not be meaningfully
//     * cloned. Construct a new Thread instead.
//     *
//     * @throws  CloneNotSupportedException
//     *          always
//     */
//    @Override
//    protected Object clone() throws CloneNotSupportedException {
//        throw new CloneNotSupportedException();
//    }
//
//    /**线程开启方法 新生状态->可运行状态
//     *
//     */
//    public synchronized void start() {
//        //线程是初始化状态，则直接抛出异常
//        //死亡状态可否
//        if (threadStatus != 0)
//            throw new IllegalThreadStateException();
//        //添加当前启动的线程到线程组
//        group.add(this);
//
//        //标记线程是否已经启动
//        boolean started = false;
//        try {
//            //调用本地方法启动线程
//            start0();
//            started = true;
//        } finally {
//            //线程未启动成功
//            try {
//                if (!started) {
//                    //将线程在线程组里标记为启动失败
//                    group.threadStartFailed(this);
//                }
//            } catch (Throwable ignore) {
//            }
//        }
//    }
//
//    private native void start0();
//
//    /**
//     *
//     */
//    @Override
//    public void run() {
//        if (target != null) {
//            target.run();
//        }
//    }
//
//    /**
//     * This method is called by the system to give a Thread
//     * a chance to clean up before it actually exits.
//     */
//    private void exit() {
//        if (group != null) {
//            group.threadTerminated(this);
//            group = null;
//        }
//        /* Aggressively null out all reference fields: see bug 4006245 */
//        target = null;
//        /* Speed the release of some of these resources */
//        threadLocals = null;
//        inheritableThreadLocals = null;
//        inheritedAccessControlContext = null;
//        blocker = null;
//        uncaughtExceptionHandler = null;
//    }
//
//    /**抛出ThreadDeath对象，终止线程
//     */
//    @Deprecated
//    public final void stop() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            checkAccess();
//            if (this != Thread.currentThread()) {
//                security.checkPermission(SecurityConstants.STOP_THREAD_PERMISSION);
//            }
//        }
//        if (threadStatus != 0) {
//            resume();
//        }
//        stop0(new ThreadDeath());
//    }
//
//    /**
//     * Throws {@code UnsupportedOperationException}.
//     *
//     * @param obj ignored
//     *
//     * @deprecated This method was originally designed to force a thread to stop
//     *        and throw a given {@code Throwable} as an exception. It was
//     *        inherently unsafe (see {@link #stop()} for details), and furthermore
//     *        could be used to generate exceptions that the target thread was
//     *        not prepared to handle.
//     *        For more information, see
//     *        <a href="{@docRoot}/../technotes/guides/concurrency/threadPrimitiveDeprecation.html">Why
//     *        are Thread.stop, Thread.suspend and Thread.resume Deprecated?</a>.
//     */
//    @Deprecated
//    public final synchronized void stop(Throwable obj) {
//        throw new UnsupportedOperationException();
//    }
//
//
//    /**interrupt():设法是中断线程，将会设置该线程的中断状态位，即设置为true，中断的结果线程是死亡、还是等待新的任务或是继续运行至下一步，
//     * 就取决于这个程序本身。线程会不时地检测这个中断标示位，以判断线程是否应该被中断（中断标示值是否为true）。
//     * 它并不像stop方法那样会中断一个正在运行的线程。
//     */
//    public void interrupt() {
//        if (this != Thread.currentThread())
//            checkAccess();
//
//        synchronized (blockerLock) {
//            Interruptible b = blocker;
//            if (b != null) {
//                interrupt0();           // Just to set the interrupt flag
//                b.interrupt(this);
//                return;
//            }
//        }
//        //调用本地方法中断线程
//        interrupt0();
//    }
//
//    /**interrupted()
//     */
//    public static boolean interrupted() {
//        return currentThread().isInterrupted(true);
//    }
//
//    /**检测当前的中断标记（类似属性的get方法）
//     */
//    public boolean isInterrupted() {
//        return isInterrupted(false);
//    }
//
//    /**
//     * Tests if some Thread has been interrupted.  The interrupted state
//     * is reset or not based on the value of ClearInterrupted that is
//     * passed.
//     */
//    private native boolean isInterrupted(boolean ClearInterrupted);
//
//    /**
//     * Throws {@link NoSuchMethodError}.
//     *
//     * @deprecated This method was originally designed to destroy this
//     *     thread without any cleanup. Any monitors it held would have
//     *     remained locked. However, the method was never implemented.
//     *     If if were to be implemented, it would be deadlock-prone in
//     *     much the manner of {@link #suspend}. If the target thread held
//     *     a lock protecting a critical system resource when it was
//     *     destroyed, no thread could ever access this resource again.
//     *     If another thread ever attempted to lock this resource, deadlock
//     *     would result. Such deadlocks typically manifest themselves as
//     *     "frozen" processes. For more information, see
//     *     <a href="{@docRoot}/../technotes/guides/concurrency/threadPrimitiveDeprecation.html">
//     *     Why are Thread.stop, Thread.suspend and Thread.resume Deprecated?</a>.
//     * @throws NoSuchMethodError always
//     */
//    @Deprecated
//    public void destroy() {
//        throw new NoSuchMethodError();
//    }
//
//    /**
//     * Tests if this thread is alive. A thread is alive if it has
//     * been started and has not yet died.
//     *
//     * @return  <code>true</code> if this thread is alive;
//     *          <code>false</code> otherwise.
//     */
//    public final native boolean isAlive();
//
//    /**
//     * Suspends this thread.
//     * <p>
//     * First, the <code>checkAccess</code> method of this thread is called
//     * with no arguments. This may result in throwing a
//     * <code>SecurityException </code>(in the current thread).
//     * <p>
//     * If the thread is alive, it is suspended and makes no further
//     * progress unless and until it is resumed.
//     *
//     * @exception  SecurityException  if the current thread cannot modify
//     *               this thread.
//     * @see #checkAccess
//     * @deprecated   This method has been deprecated, as it is
//     *   inherently deadlock-prone.  If the target thread holds a lock on the
//     *   monitor protecting a critical system resource when it is suspended, no
//     *   thread can access this resource until the target thread is resumed. If
//     *   the thread that would resume the target thread attempts to lock this
//     *   monitor prior to calling <code>resume</code>, deadlock results.  Such
//     *   deadlocks typically manifest themselves as "frozen" processes.
//     *   For more information, see
//     *   <a href="{@docRoot}/../technotes/guides/concurrency/threadPrimitiveDeprecation.html">Why
//     *   are Thread.stop, Thread.suspend and Thread.resume Deprecated?</a>.
//     */
//    @Deprecated
//    public final void suspend() {
//        checkAccess();
//        suspend0();
//    }
//
//    /**
//     * Resumes a suspended thread.
//     * <p>
//     * First, the <code>checkAccess</code> method of this thread is called
//     * with no arguments. This may result in throwing a
//     * <code>SecurityException</code> (in the current thread).
//     * <p>
//     * If the thread is alive but suspended, it is resumed and is
//     * permitted to make progress in its execution.
//     *
//     * @exception  SecurityException  if the current thread cannot modify this
//     *               thread.
//     * @see        #checkAccess
//     * @see        #suspend()
//     * @deprecated This method exists solely for use with {@link #suspend},
//     *     which has been deprecated because it is deadlock-prone.
//     *     For more information, see
//     *     <a href="{@docRoot}/../technotes/guides/concurrency/threadPrimitiveDeprecation.html">Why
//     *     are Thread.stop, Thread.suspend and Thread.resume Deprecated?</a>.
//     */
//    @Deprecated
//    public final void resume() {
//        checkAccess();
//        resume0();
//    }
//
//    /**设置线程优先级,最大10,最小1
//     *
//     */
//    public final void setPriority(int newPriority) {
//        ThreadGroup g;
//        checkAccess();
//        if (newPriority > MAX_PRIORITY || newPriority < MIN_PRIORITY) {
//            throw new IllegalArgumentException();
//        }
//        if((g = getThreadGroup()) != null) {
//            if (newPriority > g.getMaxPriority()) {
//                //子线程的优先级不能超过
//                newPriority = g.getMaxPriority();
//            }
//            //底层设置线程native方法
//            setPriority0(priority = newPriority);
//        }
//    }
//
//    /**
//     * Returns this thread's priority.
//     *
//     * @return  this thread's priority.
//     * @see     #setPriority
//     */
//    public final int getPriority() {
//        return priority;
//    }
//
//    /**
//     * Changes the name of this thread to be equal to the argument
//     * <code>name</code>.
//     * <p>
//     * First the <code>checkAccess</code> method of this thread is called
//     * with no arguments. This may result in throwing a
//     * <code>SecurityException</code>.
//     *
//     * @param      name   the new name for this thread.
//     * @exception  SecurityException  if the current thread cannot modify this
//     *               thread.
//     * @see        #getName
//     * @see        #checkAccess()
//     */
//    public final synchronized void setName(String name) {
//        checkAccess();
//        if (name == null) {
//            throw new NullPointerException("name cannot be null");
//        }
//
//        this.name = name;
//        if (threadStatus != 0) {
//            setNativeName(name);
//        }
//    }
//
//    /**
//     * Returns this thread's name.
//     *
//     * @return  this thread's name.
//     * @see     #setName(String)
//     */
//    public final String getName() {
//        return name;
//    }
//
//    /**
//     * Returns the thread group to which this thread belongs.
//     * This method returns null if this thread has died
//     * (been stopped).
//     *
//     * @return  this thread's thread group.
//     */
//    public final ThreadGroup getThreadGroup() {
//        return group;
//    }
//
//    /**
//     * Returns an estimate of the number of active threads in the current
//     * thread's {@linkplain ThreadGroup thread group} and its
//     * subgroups. Recursively iterates over all subgroups in the current
//     * thread's thread group.
//     *
//     * <p> The value returned is only an estimate because the number of
//     * threads may change dynamically while this method traverses internal
//     * data structures, and might be affected by the presence of certain
//     * system threads. This method is intended primarily for debugging
//     * and monitoring purposes.
//     *
//     * @return  an estimate of the number of active threads in the current
//     *          thread's thread group and in any other thread group that
//     *          has the current thread's thread group as an ancestor
//     */
//    public static int activeCount() {
//        return currentThread().getThreadGroup().activeCount();
//    }
//
//    /**
//     * Copies into the specified array every active thread in the current
//     * thread's thread group and its subgroups. This method simply
//     * invokes the {@link ThreadGroup#enumerate(Thread[])}
//     * method of the current thread's thread group.
//     *
//     * <p> An application might use the {@linkplain #activeCount activeCount}
//     * method to get an estimate of how big the array should be, however
//     * <i>if the array is too short to hold all the threads, the extra threads
//     * are silently ignored.</i>  If it is critical to obtain every active
//     * thread in the current thread's thread group and its subgroups, the
//     * invoker should verify that the returned int value is strictly less
//     * than the length of {@code tarray}.
//     *
//     * <p> Due to the inherent race condition in this method, it is recommended
//     * that the method only be used for debugging and monitoring purposes.
//     *
//     * @param  tarray
//     *         an array into which to put the list of threads
//     *
//     * @return  the number of threads put into the array
//     *
//     * @throws  SecurityException
//     *          if {@link ThreadGroup#checkAccess} determines that
//     *          the current thread cannot access its thread group
//     */
//    public static int enumerate(Thread tarray[]) {
//        return currentThread().getThreadGroup().enumerate(tarray);
//    }
//
//    /**
//     * Counts the number of stack frames in this thread. The thread must
//     * be suspended.
//     *
//     * @return     the number of stack frames in this thread.
//     * @exception  IllegalThreadStateException  if this thread is not
//     *             suspended.
//     * @deprecated The definition of this call depends on {@link #suspend},
//     *             which is deprecated.  Further, the results of this call
//     *             were never well-defined.
//     */
//    @Deprecated
//    public native int countStackFrames();
//    /*==================================================Join=====================================================*/
//
//    /**join()方法会一直等待线程超时或者终止，代码如下所示。
//     * join
//     */
//    public final void join() throws InterruptedException {
//        //阻塞,知道当前线程执行完成。
//        join(0);
//    }
//
//    /**join同步方法
//     */
//    public final synchronized void join(long millis)
//    throws InterruptedException {
//        //获取当前时间
//        long base = System.currentTimeMillis();
//        long now = 0;
//        //校验
//        if (millis < 0) {
//            throw new IllegalArgumentException("timeout value is negative");
//        }
//
//        if (millis == 0) {
//            //判断线程是否存活
//            while (isAlive()) {
//                //进入当前线程的等待队列中
//                wait(0);
//            }
//        } else {
//            //判断线程是否存活
//            while (isAlive()) {
//                //最多等待时间
//                long delay = millis - now;
//                if (delay <= 0) {
//                    break;
//                }
//                //进入当前thread的等待队列中
//                wait(delay);
//                now = System.currentTimeMillis() - base;
//            }
//        }
//    }
//
//    /**等待阻塞 纳秒级别,实际还是为毫秒。
//     */
//    public final synchronized void join(long millis, int nanos)
//    throws InterruptedException {
//        if (millis < 0) {
//            throw new IllegalArgumentException("timeout value is negative");
//        }
//        if (nanos < 0 || nanos > 999999) {
//            throw new IllegalArgumentException(
//                                "nanosecond timeout value out of range");
//        }
//        if (nanos >= 500000 || (nanos != 0 && millis == 0)) {
//            millis++;
//        }
//        join(millis);
//    }
//
//
//
//    /**
//     * Prints a stack trace of the current thread to the standard error stream.
//     * This method is used only for debugging.
//     *
//     * @see     Throwable#printStackTrace()
//     */
//    public static void dumpStack() {
//        new Exception("Stack trace").printStackTrace();
//    }
//
//    /**
//     * Marks this thread as either a {@linkplain #isDaemon daemon} thread
//     * or a user thread. The Java Virtual Machine exits when the only
//     * threads running are all daemon threads.
//     *
//     * <p> This method must be invoked before the thread is started.
//     *
//     * @param  on
//     *         if {@code true}, marks this thread as a daemon thread
//     *
//     * @throws  IllegalThreadStateException
//     *          if this thread is {@linkplain #isAlive alive}
//     *
//     * @throws  SecurityException
//     *          if {@link #checkAccess} determines that the current
//     *          thread cannot modify this thread
//     */
//    public final void setDaemon(boolean on) {
//        checkAccess();
//        if (isAlive()) {
//            throw new IllegalThreadStateException();
//        }
//        daemon = on;
//    }
//
//    /**
//     * Tests if this thread is a daemon thread.
//     *
//     * @return  <code>true</code> if this thread is a daemon thread;
//     *          <code>false</code> otherwise.
//     * @see     #setDaemon(boolean)
//     */
//    public final boolean isDaemon() {
//        return daemon;
//    }
//
//    /**
//     * Determines if the currently running thread has permission to
//     * modify this thread.
//     * <p>
//     * If there is a security manager, its <code>checkAccess</code> method
//     * is called with this thread as its argument. This may result in
//     * throwing a <code>SecurityException</code>.
//     *
//     * @exception  SecurityException  if the current thread is not allowed to
//     *               access this thread.
//     * @see        SecurityManager#checkAccess(Thread)
//     */
//    public final void checkAccess() {
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkAccess(this);
//        }
//    }
//
//    /**
//     * Returns a string representation of this thread, including the
//     * thread's name, priority, and thread group.
//     *
//     * @return  a string representation of this thread.
//     */
//    public String toString() {
//        ThreadGroup group = getThreadGroup();
//        if (group != null) {
//            return "Thread[" + getName() + "," + getPriority() + "," +
//                           group.getName() + "]";
//        } else {
//            return "Thread[" + getName() + "," + getPriority() + "," +
//                            "" + "]";
//        }
//    }
//
//    /**
//     * Returns the context ClassLoader for this Thread. The context
//     * ClassLoader is provided by the creator of the thread for use
//     * by code running in this thread when loading classes and resources.
//     * If not {@linkplain #setContextClassLoader set}, the default is the
//     * ClassLoader context of the parent Thread. The context ClassLoader of the
//     * primordial thread is typically set to the class loader used to load the
//     * application.
//     *
//     * <p>If a security manager is present, and the invoker's class loader is not
//     * {@code null} and is not the same as or an ancestor of the context class
//     * loader, then this method invokes the security manager's {@link
//     * SecurityManager#checkPermission(java.security.Permission) checkPermission}
//     * method with a {@link RuntimePermission RuntimePermission}{@code
//     * ("getClassLoader")} permission to verify that retrieval of the context
//     * class loader is permitted.
//     *
//     * @return  the context ClassLoader for this Thread, or {@code null}
//     *          indicating the system class loader (or, failing that, the
//     *          bootstrap class loader)
//     *
//     * @throws  SecurityException
//     *          if the current thread cannot get the context ClassLoader
//     *
//     * @since 1.2
//     */
//    @CallerSensitive
//    public ClassLoader getContextClassLoader() {
//        if (contextClassLoader == null)
//            return null;
//        SecurityManager sm = System.getSecurityManager();
//        if (sm != null) {
//            ClassLoader.checkClassLoaderPermission(contextClassLoader,
//                                                   Reflection.getCallerClass());
//        }
//        return contextClassLoader;
//    }
//
//    /**
//     * Sets the context ClassLoader for this Thread. The context
//     * ClassLoader can be set when a thread is created, and allows
//     * the creator of the thread to provide the appropriate class loader,
//     * through {@code getContextClassLoader}, to code running in the thread
//     * when loading classes and resources.
//     *
//     * <p>If a security manager is present, its {@link
//     * SecurityManager#checkPermission(java.security.Permission) checkPermission}
//     * method is invoked with a {@link RuntimePermission RuntimePermission}{@code
//     * ("setContextClassLoader")} permission to see if setting the context
//     * ClassLoader is permitted.
//     *
//     * @param  cl
//     *         the context ClassLoader for this Thread, or null  indicating the
//     *         system class loader (or, failing that, the bootstrap class loader)
//     *
//     * @throws  SecurityException
//     *          if the current thread cannot set the context ClassLoader
//     *
//     * @since 1.2
//     */
//    public void setContextClassLoader(ClassLoader cl) {
//        SecurityManager sm = System.getSecurityManager();
//        if (sm != null) {
//            sm.checkPermission(new RuntimePermission("setContextClassLoader"));
//        }
//        contextClassLoader = cl;
//    }
//
//    /**
//     * Returns <tt>true</tt> if and only if the current thread holds the
//     * monitor lock on the specified object.
//     *
//     * <p>This method is designed to allow a program to assert that
//     * the current thread already holds a specified lock:
//     * <pre>
//     *     assert Thread.holdsLock(obj);
//     * </pre>
//     *
//     * @param  obj the object on which to test lock ownership
//     * @throws NullPointerException if obj is <tt>null</tt>
//     * @return <tt>true</tt> if the current thread holds the monitor lock on
//     *         the specified object.
//     * @since 1.4
//     */
//    public static native boolean holdsLock(Object obj);
//
//    private static final StackTraceElement[] EMPTY_STACK_TRACE
//        = new StackTraceElement[0];
//
//    /**
//     * Returns an array of stack trace elements representing the stack dump
//     * of this thread.  This method will return a zero-length array if
//     * this thread has not started, has started but has not yet been
//     * scheduled to run by the system, or has terminated.
//     * If the returned array is of non-zero length then the first element of
//     * the array represents the top of the stack, which is the most recent
//     * method invocation in the sequence.  The last element of the array
//     * represents the bottom of the stack, which is the least recent method
//     * invocation in the sequence.
//     *
//     * <p>If there is a security manager, and this thread is not
//     * the current thread, then the security manager's
//     * <tt>checkPermission</tt> method is called with a
//     * <tt>RuntimePermission("getStackTrace")</tt> permission
//     * to see if it's ok to get the stack trace.
//     *
//     * <p>Some virtual machines may, under some circumstances, omit one
//     * or more stack frames from the stack trace.  In the extreme case,
//     * a virtual machine that has no stack trace information concerning
//     * this thread is permitted to return a zero-length array from this
//     * method.
//     *
//     * @return an array of <tt>StackTraceElement</tt>,
//     * each represents one stack frame.
//     *
//     * @throws SecurityException
//     *        if a security manager exists and its
//     *        <tt>checkPermission</tt> method doesn't allow
//     *        getting the stack trace of thread.
//     * @see SecurityManager#checkPermission
//     * @see RuntimePermission
//     * @see Throwable#getStackTrace
//     *
//     * @since 1.5
//     */
//    public StackTraceElement[] getStackTrace() {
//        if (this != Thread.currentThread()) {
//            // check for getStackTrace permission
//            SecurityManager security = System.getSecurityManager();
//            if (security != null) {
//                security.checkPermission(
//                    SecurityConstants.GET_STACK_TRACE_PERMISSION);
//            }
//            // optimization so we do not call into the vm for threads that
//            // have not yet started or have terminated
//            if (!isAlive()) {
//                return EMPTY_STACK_TRACE;
//            }
//            StackTraceElement[][] stackTraceArray = dumpThreads(new Thread[] {this});
//            StackTraceElement[] stackTrace = stackTraceArray[0];
//            // a thread that was alive during the previous isAlive call may have
//            // since terminated, therefore not having a stacktrace.
//            if (stackTrace == null) {
//                stackTrace = EMPTY_STACK_TRACE;
//            }
//            return stackTrace;
//        } else {
//            // Don't need JVM help for current thread
//            return (new Exception()).getStackTrace();
//        }
//    }
//
//    /**
//     * Returns a map of stack traces for all live threads.
//     * The map keys are threads and each map value is an array of
//     * <tt>StackTraceElement</tt> that represents the stack dump
//     * of the corresponding <tt>Thread</tt>.
//     * The returned stack traces are in the format specified for
//     * the {@link #getStackTrace getStackTrace} method.
//     *
//     * <p>The threads may be executing while this method is called.
//     * The stack trace of each thread only represents a snapshot and
//     * each stack trace may be obtained at different time.  A zero-length
//     * array will be returned in the map value if the virtual machine has
//     * no stack trace information about a thread.
//     *
//     * <p>If there is a security manager, then the security manager's
//     * <tt>checkPermission</tt> method is called with a
//     * <tt>RuntimePermission("getStackTrace")</tt> permission as well as
//     * <tt>RuntimePermission("modifyThreadGroup")</tt> permission
//     * to see if it is ok to get the stack trace of all threads.
//     *
//     * @return a <tt>Map</tt> from <tt>Thread</tt> to an array of
//     * <tt>StackTraceElement</tt> that represents the stack trace of
//     * the corresponding thread.
//     *
//     * @throws SecurityException
//     *        if a security manager exists and its
//     *        <tt>checkPermission</tt> method doesn't allow
//     *        getting the stack trace of thread.
//     * @see #getStackTrace
//     * @see SecurityManager#checkPermission
//     * @see RuntimePermission
//     * @see Throwable#getStackTrace
//     *
//     * @since 1.5
//     */
//    public static Map<Thread, StackTraceElement[]> getAllStackTraces() {
//        // check for getStackTrace permission
//        SecurityManager security = System.getSecurityManager();
//        if (security != null) {
//            security.checkPermission(
//                SecurityConstants.GET_STACK_TRACE_PERMISSION);
//            security.checkPermission(
//                SecurityConstants.MODIFY_THREADGROUP_PERMISSION);
//        }
//
//        // Get a snapshot of the list of all threads
//        Thread[] threads = getThreads();
//        StackTraceElement[][] traces = dumpThreads(threads);
//        Map<Thread, StackTraceElement[]> m = new HashMap<>(threads.length);
//        for (int i = 0; i < threads.length; i++) {
//            StackTraceElement[] stackTrace = traces[i];
//            if (stackTrace != null) {
//                m.put(threads[i], stackTrace);
//            }
//            // else terminated so we don't put it in the map
//        }
//        return m;
//    }
//
//
//    private static final RuntimePermission SUBCLASS_IMPLEMENTATION_PERMISSION =
//                    new RuntimePermission("enableContextClassLoaderOverride");
//
//    /** cache of subclass security audit results */
//    /* Replace with ConcurrentReferenceHashMap when/if it appears in a future
//     * release */
//    private static class Caches {
//        /** cache of subclass security audit results */
//        static final ConcurrentMap<WeakClassKey,Boolean> subclassAudits =
//            new ConcurrentHashMap<>();
//
//        /** queue for WeakReferences to audited subclasses */
//        static final ReferenceQueue<Class<?>> subclassAuditsQueue =
//            new ReferenceQueue<>();
//    }
//
//    /**
//     * Verifies that this (possibly subclass) instance can be constructed
//     * without violating security constraints: the subclass must not override
//     * security-sensitive non-final methods, or else the
//     * "enableContextClassLoaderOverride" RuntimePermission is checked.
//     */
//    private static boolean isCCLOverridden(Class<?> cl) {
//        if (cl == Thread.class)
//            return false;
//
//        processQueue(Caches.subclassAuditsQueue, Caches.subclassAudits);
//        WeakClassKey key = new WeakClassKey(cl, Caches.subclassAuditsQueue);
//        Boolean result = Caches.subclassAudits.get(key);
//        if (result == null) {
//            result = Boolean.valueOf(auditSubclass(cl));
//            Caches.subclassAudits.putIfAbsent(key, result);
//        }
//
//        return result.booleanValue();
//    }
//
//    /**
//     * Performs reflective checks on given subclass to verify that it doesn't
//     * override security-sensitive non-final methods.  Returns true if the
//     * subclass overrides any of the methods, false otherwise.
//     */
//    private static boolean auditSubclass(final Class<?> subcl) {
//        Boolean result = AccessController.doPrivileged(
//            new PrivilegedAction<Boolean>() {
//                public Boolean run() {
//                    for (Class<?> cl = subcl;
//                         cl != Thread.class;
//                         cl = cl.getSuperclass())
//                    {
//                        try {
//                            cl.getDeclaredMethod("getContextClassLoader", new Class<?>[0]);
//                            return Boolean.TRUE;
//                        } catch (NoSuchMethodException ex) {
//                        }
//                        try {
//                            Class<?>[] params = {ClassLoader.class};
//                            cl.getDeclaredMethod("setContextClassLoader", params);
//                            return Boolean.TRUE;
//                        } catch (NoSuchMethodException ex) {
//                        }
//                    }
//                    return Boolean.FALSE;
//                }
//            }
//        );
//        return result.booleanValue();
//    }
//
//    private native static StackTraceElement[][] dumpThreads(Thread[] threads);
//    private native static Thread[] getThreads();
//
//    /**
//     * Returns the identifier of this Thread.  The thread ID is a positive
//     * <tt>long</tt> number generated when this thread was created.
//     * The thread ID is unique and remains unchanged during its lifetime.
//     * When a thread is terminated, this thread ID may be reused.
//     *
//     * @return this thread's ID.
//     * @since 1.5
//     */
//    public long getId() {
//        return tid;
//    }
//
//    /**
//     * A thread state.  A thread can be in one of the following states:
//     * <ul>
//     * <li>{@link #NEW}<br>
//     *     A thread that has not yet started is in this state.
//     *     </li>
//     * <li>{@link #RUNNABLE}<br>
//     *     A thread executing in the Java virtual machine is in this state.
//     *     </li>
//     * <li>{@link #BLOCKED}<br>
//     *     A thread that is blocked waiting for a monitor lock
//     *     is in this state.
//     *     </li>
//     * <li>{@link #WAITING}<br>
//     *     A thread that is waiting indefinitely for another thread to
//     *     perform a particular action is in this state.
//     *     </li>
//     * <li>{@link #TIMED_WAITING}<br>
//     *     A thread that is waiting for another thread to perform an action
//     *     for up to a specified waiting time is in this state.
//     *     </li>
//     * <li>{@link #TERMINATED}<br>
//     *     A thread that has exited is in this state.
//     *     </li>
//     * </ul>
//     *
//     * <p>
//     * A thread can be in only one state at a given point in time.
//     * These states are virtual machine states which do not reflect
//     * any operating system thread states.
//     *
//     * @since   1.5
//     * @see #getState
//     */
//    public enum State {
//        //初始化状态
//        NEW,
//        //可运行状态，此时的可运行包括运行中的状态和就绪状态
//        RUNNABLE,
//        //线程阻塞状态
//        BLOCKED,
//        //等待状态
//        WAITING,
//        //超时等待状态
//        TIMED_WAITING,
//        //线程终止状态
//        TERMINATED;
//    }
//
//    /**
//     * Returns the state of this thread.
//     * This method is designed for use in monitoring of the system state,
//     * not for synchronization control.
//     *
//     * @return this thread's state.
//     * @since 1.5
//     */
//    public State getState() {
//        // get current thread state
//        return sun.misc.VM.toThreadState(threadStatus);
//    }
//
//    // Added in JSR-166
//
//    /**
//     * Interface for handlers invoked when a <tt>Thread</tt> abruptly
//     * terminates due to an uncaught exception.
//     * <p>When a thread is about to terminate due to an uncaught exception
//     * the Java Virtual Machine will query the thread for its
//     * <tt>UncaughtExceptionHandler</tt> using
//     * {@link #getUncaughtExceptionHandler} and will invoke the handler's
//     * <tt>uncaughtException</tt> method, passing the thread and the
//     * exception as arguments.
//     * If a thread has not had its <tt>UncaughtExceptionHandler</tt>
//     * explicitly set, then its <tt>ThreadGroup</tt> object acts as its
//     * <tt>UncaughtExceptionHandler</tt>. If the <tt>ThreadGroup</tt> object
//     * has no
//     * special requirements for dealing with the exception, it can forward
//     * the invocation to the {@linkplain #getDefaultUncaughtExceptionHandler
//     * default uncaught exception handler}.
//     *
//     * @see #setDefaultUncaughtExceptionHandler
//     * @see #setUncaughtExceptionHandler
//     * @see ThreadGroup#uncaughtException
//     * @since 1.5
//     */
//    @FunctionalInterface
//    public interface UncaughtExceptionHandler {
//        /**
//         * Method invoked when the given thread terminates due to the
//         * given uncaught exception.
//         * <p>Any exception thrown by this method will be ignored by the
//         * Java Virtual Machine.
//         * @param t the thread
//         * @param e the exception
//         */
//        void uncaughtException(Thread t, Throwable e);
//    }
//
//    // null unless explicitly set
//    private volatile UncaughtExceptionHandler uncaughtExceptionHandler;
//
//    // null unless explicitly set
//    private static volatile UncaughtExceptionHandler defaultUncaughtExceptionHandler;
//
//    /**
//     * Set the default handler invoked when a thread abruptly terminates
//     * due to an uncaught exception, and no other handler has been defined
//     * for that thread.
//     *
//     * <p>Uncaught exception handling is controlled first by the thread, then
//     * by the thread's {@link ThreadGroup} object and finally by the default
//     * uncaught exception handler. If the thread does not have an explicit
//     * uncaught exception handler set, and the thread's thread group
//     * (including parent thread groups)  does not specialize its
//     * <tt>uncaughtException</tt> method, then the default handler's
//     * <tt>uncaughtException</tt> method will be invoked.
//     * <p>By setting the default uncaught exception handler, an application
//     * can change the way in which uncaught exceptions are handled (such as
//     * logging to a specific device, or file) for those threads that would
//     * already accept whatever &quot;default&quot; behavior the system
//     * provided.
//     *
//     * <p>Note that the default uncaught exception handler should not usually
//     * defer to the thread's <tt>ThreadGroup</tt> object, as that could cause
//     * infinite recursion.
//     *
//     * @param eh the object to use as the default uncaught exception handler.
//     * If <tt>null</tt> then there is no default handler.
//     *
//     * @throws SecurityException if a security manager is present and it
//     *         denies <tt>{@link RuntimePermission}
//     *         (&quot;setDefaultUncaughtExceptionHandler&quot;)</tt>
//     *
//     * @see #setUncaughtExceptionHandler
//     * @see #getUncaughtExceptionHandler
//     * @see ThreadGroup#uncaughtException
//     * @since 1.5
//     */
//    public static void setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
//        SecurityManager sm = System.getSecurityManager();
//        if (sm != null) {
//            sm.checkPermission(
//                new RuntimePermission("setDefaultUncaughtExceptionHandler")
//                    );
//        }
//
//         defaultUncaughtExceptionHandler = eh;
//     }
//
//    /**
//     * Returns the default handler invoked when a thread abruptly terminates
//     * due to an uncaught exception. If the returned value is <tt>null</tt>,
//     * there is no default.
//     * @since 1.5
//     * @see #setDefaultUncaughtExceptionHandler
//     * @return the default uncaught exception handler for all threads
//     */
//    public static UncaughtExceptionHandler getDefaultUncaughtExceptionHandler(){
//        return defaultUncaughtExceptionHandler;
//    }
//
//    /**
//     * Returns the handler invoked when this thread abruptly terminates
//     * due to an uncaught exception. If this thread has not had an
//     * uncaught exception handler explicitly set then this thread's
//     * <tt>ThreadGroup</tt> object is returned, unless this thread
//     * has terminated, in which case <tt>null</tt> is returned.
//     * @since 1.5
//     * @return the uncaught exception handler for this thread
//     */
//    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
//        return uncaughtExceptionHandler != null ?
//            uncaughtExceptionHandler : group;
//    }
//
//    /**
//     * Set the handler invoked when this thread abruptly terminates
//     * due to an uncaught exception.
//     * <p>A thread can take full control of how it responds to uncaught
//     * exceptions by having its uncaught exception handler explicitly set.
//     * If no such handler is set then the thread's <tt>ThreadGroup</tt>
//     * object acts as its handler.
//     * @param eh the object to use as this thread's uncaught exception
//     * handler. If <tt>null</tt> then this thread has no explicit handler.
//     * @throws  SecurityException  if the current thread is not allowed to
//     *          modify this thread.
//     * @see #setDefaultUncaughtExceptionHandler
//     * @see ThreadGroup#uncaughtException
//     * @since 1.5
//     */
//    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
//        checkAccess();
//        uncaughtExceptionHandler = eh;
//    }
//
//    /**
//     * Dispatch an uncaught exception to the handler. This method is
//     * intended to be called only by the JVM.
//     */
//    private void dispatchUncaughtException(Throwable e) {
//        getUncaughtExceptionHandler().uncaughtException(this, e);
//    }
//
//    /**
//     * Removes from the specified map any keys that have been enqueued
//     * on the specified reference queue.
//     */
//    static void processQueue(ReferenceQueue<Class<?>> queue,
//                             ConcurrentMap<? extends
//                             WeakReference<Class<?>>, ?> map)
//    {
//        Reference<? extends Class<?>> ref;
//        while((ref = queue.poll()) != null) {
//            map.remove(ref);
//        }
//    }
//
//    /**
//     *  Weak key for Class objects.
//     **/
//    static class WeakClassKey extends WeakReference<Class<?>> {
//        /**
//         * saved value of the referent's identity hash code, to maintain
//         * a consistent hash code after the referent has been cleared
//         */
//        private final int hash;
//
//        /**
//         * Create a new WeakClassKey to the given object, registered
//         * with a queue.
//         */
//        WeakClassKey(Class<?> cl, ReferenceQueue<Class<?>> refQueue) {
//            super(cl, refQueue);
//            hash = System.identityHashCode(cl);
//        }
//
//        /**
//         * Returns the identity hash code of the original referent.
//         */
//        @Override
//        public int hashCode() {
//            return hash;
//        }
//
//        /**
//         * Returns true if the given object is this identical
//         * WeakClassKey instance, or, if this object's referent has not
//         * been cleared, if the given object is another WeakClassKey
//         * instance with the identical non-null referent as this one.
//         */
//        @Override
//        public boolean equals(Object obj) {
//            if (obj == this)
//                return true;
//
//            if (obj instanceof WeakClassKey) {
//                Object referent = get();
//                return (referent != null) &&
//                       (referent == ((WeakClassKey) obj).get());
//            } else {
//                return false;
//            }
//        }
//    }
//
//
//    // The following three initially uninitialized fields are exclusively
//    // managed by class java.util.concurrent.ThreadLocalRandom. These
//    // fields are used to build the high-performance PRNGs in the
//    // concurrent code, and we can not risk accidental false sharing.
//    // Hence, the fields are isolated with @Contended.
//
//    /** The current seed for a ThreadLocalRandom */
//    @sun.misc.Contended("tlr")
//    long threadLocalRandomSeed;
//
//    /** Probe hash value; nonzero if threadLocalRandomSeed initialized */
//    @sun.misc.Contended("tlr")
//    int threadLocalRandomProbe;
//
//    /** Secondary seed isolated from public ThreadLocalRandom sequence */
//    @sun.misc.Contended("tlr")
//    int threadLocalRandomSecondarySeed;
//
//    /* Some private helper methods */
//    private native void setPriority0(int newPriority);
//    private native void stop0(Object o);
//    private native void suspend0();
//    private native void resume0();
//    private native void interrupt0();
//    private native void setNativeName(String name);
//}
