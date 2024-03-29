///*
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
///*
// *
// *
// *
// *
// *
// * Written by Doug Lea with assistance from members of JCP JSR-166
// * Expert Group and released to the public domain, as explained at
// * http://creativecommons.org/publicdomain/zero/1.0/
// */
//
//package com.padingpading.interview.thread.wangwenjun.threadpool.sourcecode;
//
//import sun.security.util3.SecurityConstants;
//
//import java.security.AccessControlContext;
//import java.security.AccessControlException;
//import java.security.AccessController;
//import java.security.PrivilegedAction;
//import java.security.PrivilegedActionException;
//import java.security.PrivilegedExceptionAction;
//import java.util.Collection;
//import java.util.List;
//import java.util.concurrent.AbstractExecutorService;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executor;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.ForkJoinPool;
//import java.util.concurrent.Future;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.SynchronousQueue;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**工厂:Executor、ExecutorService、AbstractExecutorService、ThreadFactory、CallAble
// */
//public class Executors {
//
//    /**线程:核心线程=最大线程
//     * 队列:无界队列。
//     * 拒绝策略:直接抛异常
//     */
//    public static java.util.concurrent.ExecutorService newFixedThreadPool(int nThreads) {
//        return new java.util.concurrent.ThreadPoolExecutor(nThreads, nThreads,
//                                      0L, TimeUnit.MILLISECONDS,
//                                      new LinkedBlockingQueue<Runnable>());
//    }
//
//    /**
//     * Creates a thread pool that maintains enough threads to support
//     * the given parallelism level, and may use multiple queues to
//     * reduce contention. The parallelism level corresponds to the
//     * maximum number of threads actively engaged in, or available to
//     * engage in, task processing. The actual number of threads may
//     * grow and shrink dynamically. A work-stealing pool makes no
//     * guarantees about the order in which submitted tasks are
//     * executed.
//     *
//     * @param parallelism the targeted parallelism level
//     * @return the newly created thread pool
//     * @throws IllegalArgumentException if {@code parallelism <= 0}
//     * @since 1.8
//     */
//    public static java.util.concurrent.ExecutorService newWorkStealingPool(int parallelism) {
//        return new ForkJoinPool
//            (parallelism,
//             ForkJoinPool.defaultForkJoinWorkerThreadFactory,
//             null, true);
//    }
//
//    /**
//     * Creates a work-stealing thread pool using all
//     * {@link Runtime#availableProcessors available processors}
//     * as its target parallelism level.
//     * @return the newly created thread pool
//     * @see #newWorkStealingPool(int)
//     * @since 1.8
//     */
//    public static java.util.concurrent.ExecutorService newWorkStealingPool() {
//        return new ForkJoinPool
//            (Runtime.getRuntime().availableProcessors(),
//             ForkJoinPool.defaultForkJoinWorkerThreadFactory,
//             null, true);
//    }
//
//    /**线程:核心线程=最大线程
//     * 队列:无界队列。
//     * 拒绝策略:直接抛异常
//     */
//    public static java.util.concurrent.ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
//        return new java.util.concurrent.ThreadPoolExecutor(nThreads, nThreads,
//                                      0L, TimeUnit.MILLISECONDS,
//                                      new LinkedBlockingQueue<Runnable>(),
//                                      threadFactory);
//    }
//
//    /**线程:核心线程=最大线程=1
//     * 队列:无界队列。
//     * 拒绝策略:直接抛异常
//     */
//    public static java.util.concurrent.ExecutorService newSingleThreadExecutor() {
//        return new FinalizableDelegatedExecutorService
//            (new java.util.concurrent.ThreadPoolExecutor(1, 1,
//                                    0L, TimeUnit.MILLISECONDS,
//                                    new LinkedBlockingQueue<Runnable>()));
//    }
//
//    /**线程:核心线程=最大线程=1
//     * 队列:无界队列。
//     * 拒绝策略:直接抛异常
//     */
//    public static java.util.concurrent.ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
//        return new FinalizableDelegatedExecutorService
//            (new java.util.concurrent.ThreadPoolExecutor(1, 1,
//                                    0L, TimeUnit.MILLISECONDS,
//                                    new LinkedBlockingQueue<Runnable>(),
//                                    threadFactory));
//    }
//
//    /**线程:核心线程=0,最大线程数=MAX。只要队列中有,就立马就要去消息,所以核心线程数为0。
//     * 队列:同步队列,生产者和消费者 里面只会有一个元素。
//     * 拒绝策略:直接抛异常
//     * 任务一提交就会创建线程,使用同步队列,SynchronousQueue有没有消息的任务,主线程向SynchronousQueue添加的时候就会被阻塞
//     * 所以核心线程数变为0,每次去新建新的线程
//     */
//    public static java.util.concurrent.ExecutorService newCachedThreadPool() {
//        return new java.util.concurrent.ThreadPoolExecutor(0, Integer.MAX_VALUE,
//                                      60L, TimeUnit.SECONDS,
//                                      new SynchronousQueue<Runnable>());
//    }
//
//    /**
//     * Creates a thread pool that creates new threads as needed, but
//     * will reuse previously constructed threads when they are
//     * available, and uses the provided
//     * ThreadFactory to create new threads when needed.
//     * @param threadFactory the factory to use when creating new threads
//     * @return the newly created thread pool
//     * @throws NullPointerException if threadFactory is null
//     */
//    public static java.util.concurrent.ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
//        return new java.util.concurrent.ThreadPoolExecutor(0, Integer.MAX_VALUE,
//                                      60L, TimeUnit.SECONDS,
//                                      new SynchronousQueue<Runnable>(),
//                                      threadFactory);
//    }
//
//    /**
//     * Creates a single-threaded executor that can schedule commands
//     * to run after a given delay, or to execute periodically.
//     * (Note however that if this single
//     * thread terminates due to a failure during execution prior to
//     * shutdown, a new one will take its place if needed to execute
//     * subsequent tasks.)  Tasks are guaranteed to execute
//     * sequentially, and no more than one task will be active at any
//     * given time. Unlike the otherwise equivalent
//     * {@code newScheduledThreadPool(1)} the returned executor is
//     * guaranteed not to be reconfigurable to use additional threads.
//     * @return the newly created scheduled executor
//     */
//    public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
//        return new DelegatedScheduledExecutorService
//            (new ScheduledThreadPoolExecutor(1));
//    }
//
//    /**
//     * Creates a single-threaded executor that can schedule commands
//     * to run after a given delay, or to execute periodically.  (Note
//     * however that if this single thread terminates due to a failure
//     * during execution prior to shutdown, a new one will take its
//     * place if needed to execute subsequent tasks.)  Tasks are
//     * guaranteed to execute sequentially, and no more than one task
//     * will be active at any given time. Unlike the otherwise
//     * equivalent {@code newScheduledThreadPool(1, threadFactory)}
//     * the returned executor is guaranteed not to be reconfigurable to
//     * use additional threads.
//     * @param threadFactory the factory to use when creating new
//     * threads
//     * @return a newly created scheduled executor
//     * @throws NullPointerException if threadFactory is null
//     */
//    public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
//        return new DelegatedScheduledExecutorService
//            (new ScheduledThreadPoolExecutor(1, threadFactory));
//    }
//
//    /**
//     * Creates a thread pool that can schedule commands to run after a
//     * given delay, or to execute periodically.
//     * @param corePoolSize the number of threads to keep in the pool,
//     * even if they are idle
//     * @return a newly created scheduled thread pool
//     * @throws IllegalArgumentException if {@code corePoolSize < 0}
//     */
//    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
//        return new ScheduledThreadPoolExecutor(corePoolSize);
//    }
//
//    /**
//     * Creates a thread pool that can schedule commands to run after a
//     * given delay, or to execute periodically.
//     * @param corePoolSize the number of threads to keep in the pool,
//     * even if they are idle
//     * @param threadFactory the factory to use when the executor
//     * creates a new thread
//     * @return a newly created scheduled thread pool
//     * @throws IllegalArgumentException if {@code corePoolSize < 0}
//     * @throws NullPointerException if threadFactory is null
//     */
//    public static ScheduledExecutorService newScheduledThreadPool(
//            int corePoolSize, ThreadFactory threadFactory) {
//        return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
//    }
//
//    /**
//     * Returns an object that delegates all defined {@link
//     * java.util.concurrent.ExecutorService} methods to the given executor, but not any
//     * other methods that might otherwise be accessible using
//     * casts. This provides a way to safely "freeze" configuration and
//     * disallow tuning of a given concrete implementation.
//     * @param executor the underlying implementation
//     * @return an {@code ExecutorService} instance
//     * @throws NullPointerException if executor null
//     */
//    public static java.util.concurrent.ExecutorService unconfigurableExecutorService(
//            java.util.concurrent.ExecutorService executor) {
//        if (executor == null)
//            throw new NullPointerException();
//        return new DelegatedExecutorService(executor);
//    }
//
//    /**
//     * Returns an object that delegates all defined {@link
//     * ScheduledExecutorService} methods to the given executor, but
//     * not any other methods that might otherwise be accessible using
//     * casts. This provides a way to safely "freeze" configuration and
//     * disallow tuning of a given concrete implementation.
//     * @param executor the underlying implementation
//     * @return a {@code ScheduledExecutorService} instance
//     * @throws NullPointerException if executor null
//     */
//    public static ScheduledExecutorService unconfigurableScheduledExecutorService(ScheduledExecutorService executor) {
//        if (executor == null)
//            throw new NullPointerException();
//        return new DelegatedScheduledExecutorService(executor);
//    }
//
//    /**
//     * Returns a default thread factory used to create new threads.
//     * This factory creates all new threads used by an Executor in the
//     * same {@link ThreadGroup}. If there is a {@link
//     * SecurityManager}, it uses the group of {@link
//     * System#getSecurityManager}, else the group of the thread
//     * invoking this {@code defaultThreadFactory} method. Each new
//     * thread is created as a non-daemon thread with priority set to
//     * the smaller of {@code Thread.NORM_PRIORITY} and the maximum
//     * priority permitted in the thread group.  New threads have names
//     * accessible via {@link Thread#getName} of
//     * <em>pool-N-thread-M</em>, where <em>N</em> is the sequence
//     * number of this factory, and <em>M</em> is the sequence number
//     * of the thread created by this factory.
//     * @return a thread factory
//     */
//    public static ThreadFactory defaultThreadFactory() {
//        return new DefaultThreadFactory();
//    }
//
//    /**
//     * Returns a thread factory used to create new threads that
//     * have the same permissions as the current thread.
//     * This factory creates threads with the same settings as {@link
//     * Executors#defaultThreadFactory}, additionally setting the
//     * AccessControlContext and contextClassLoader of new threads to
//     * be the same as the thread invoking this
//     * {@code privilegedThreadFactory} method.  A new
//     * {@code privilegedThreadFactory} can be created within an
//     * {@link AccessController#doPrivileged AccessController.doPrivileged}
//     * action setting the current thread's access control context to
//     * create threads with the selected permission settings holding
//     * within that action.
//     *
//     * <p>Note that while tasks running within such threads will have
//     * the same access control and class loader settings as the
//     * current thread, they need not have the same {@link
//     * ThreadLocal} or {@link
//     * InheritableThreadLocal} values. If necessary,
//     * particular values of thread locals can be set or reset before
//     * any task runs in {@link java.util.concurrent.ThreadPoolExecutor} subclasses using
//     * {@link ThreadPoolExecutor#beforeExecute(Thread, Runnable)}.
//     * Also, if it is necessary to initialize worker threads to have
//     * the same InheritableThreadLocal settings as some other
//     * designated thread, you can create a custom ThreadFactory in
//     * which that thread waits for and services requests to create
//     * others that will inherit its values.
//     *
//     * @return a thread factory
//     * @throws AccessControlException if the current access control
//     * context does not have permission to both get and set context
//     * class loader
//     */
//    public static ThreadFactory privilegedThreadFactory() {
//        return new PrivilegedThreadFactory();
//    }
//
//    /**适配器模式,将一个Runnable类型对象适配成Callable类型。
//     *因为Runnable接口没有返回值, 所以为了与Callable兼容, 我们额外传入了一个result参数,
//     * 使得返回的Callable对象的call方法直接执行Runnable的run方法, 然后返回传入的result参数。
//     */
//    public static <T> Callable<T> callable(Runnable task, T result) {
//        if (task == null)
//            throw new NullPointerException();
//        return new RunnableAdapter<T>(task, result);
//    }
//
//    /**
//     * Returns a {@link Callable} object that, when
//     * called, runs the given task and returns {@code null}.
//     * @param task the task to run
//     * @return a callable object
//     * @throws NullPointerException if task null
//     */
//    public static Callable<Object> callable(Runnable task) {
//        if (task == null)
//            throw new NullPointerException();
//        return new RunnableAdapter<Object>(task, null);
//    }
//
//    /**
//     * Returns a {@link Callable} object that, when
//     * called, runs the given privileged action and returns its result.
//     * @param action the privileged action to run
//     * @return a callable object
//     * @throws NullPointerException if action null
//     */
//    public static Callable<Object> callable(final PrivilegedAction<?> action) {
//        if (action == null)
//            throw new NullPointerException();
//        return new Callable<Object>() {
//            public Object call() { return action.run(); }};
//    }
//
//    /**
//     * Returns a {@link Callable} object that, when
//     * called, runs the given privileged exception action and returns
//     * its result.
//     * @param action the privileged exception action to run
//     * @return a callable object
//     * @throws NullPointerException if action null
//     */
//    public static Callable<Object> callable(final PrivilegedExceptionAction<?> action) {
//        if (action == null)
//            throw new NullPointerException();
//        return new Callable<Object>() {
//            public Object call() throws Exception { return action.run(); }};
//    }
//
//    /**
//     * Returns a {@link Callable} object that will, when called,
//     * execute the given {@code callable} under the current access
//     * control context. This method should normally be invoked within
//     * an {@link AccessController#doPrivileged AccessController.doPrivileged}
//     * action to create callables that will, if possible, execute
//     * under the selected permission settings holding within that
//     * action; or if not possible, throw an associated {@link
//     * AccessControlException}.
//     * @param callable the underlying task
//     * @param <T> the type of the callable's result
//     * @return a callable object
//     * @throws NullPointerException if callable null
//     */
//    public static <T> Callable<T> privilegedCallable(Callable<T> callable) {
//        if (callable == null)
//            throw new NullPointerException();
//        return new PrivilegedCallable<T>(callable);
//    }
//
//    /**
//     * Returns a {@link Callable} object that will, when called,
//     * execute the given {@code callable} under the current access
//     * control context, with the current context class loader as the
//     * context class loader. This method should normally be invoked
//     * within an
//     * {@link AccessController#doPrivileged AccessController.doPrivileged}
//     * action to create callables that will, if possible, execute
//     * under the selected permission settings holding within that
//     * action; or if not possible, throw an associated {@link
//     * AccessControlException}.
//     *
//     * @param callable the underlying task
//     * @param <T> the type of the callable's result
//     * @return a callable object
//     * @throws NullPointerException if callable null
//     * @throws AccessControlException if the current access control
//     * context does not have permission to both set and get context
//     * class loader
//     */
//    public static <T> Callable<T> privilegedCallableUsingCurrentClassLoader(Callable<T> callable) {
//        if (callable == null)
//            throw new NullPointerException();
//        return new PrivilegedCallableUsingCurrentClassLoader<T>(callable);
//    }
//
//    // Non-public classes supporting the public methods
//
//    /**
//     * A callable that runs given task and returns given result
//     */
//    static final class RunnableAdapter<T> implements Callable<T> {
//        final Runnable task;
//        final T result;
//        RunnableAdapter(Runnable task, T result) {
//            this.task = task;
//            this.result = result;
//        }
//        public T call() {
//            task.run();
//            return result;
//        }
//    }
//
//    /**
//     * A callable that runs under established access control settings
//     */
//    static final class PrivilegedCallable<T> implements Callable<T> {
//        private final Callable<T> task;
//        private final AccessControlContext acc;
//
//        PrivilegedCallable(Callable<T> task) {
//            this.task = task;
//            this.acc = AccessController.getContext();
//        }
//
//        public T call() throws Exception {
//            try {
//                return AccessController.doPrivileged(
//                    new PrivilegedExceptionAction<T>() {
//                        public T run() throws Exception {
//                            return task.call();
//                        }
//                    }, acc);
//            } catch (PrivilegedActionException e) {
//                throw e.getException();
//            }
//        }
//    }
//
//    /**
//     * A callable that runs under established access control settings and
//     * current ClassLoader
//     */
//    static final class PrivilegedCallableUsingCurrentClassLoader<T> implements Callable<T> {
//        private final Callable<T> task;
//        private final AccessControlContext acc;
//        private final ClassLoader ccl;
//
//        PrivilegedCallableUsingCurrentClassLoader(Callable<T> task) {
//            SecurityManager sm = System.getSecurityManager();
//            if (sm != null) {
//                // Calls to getContextClassLoader from this class
//                // never trigger a security check, but we check
//                // whether our callers have this permission anyways.
//                sm.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
//
//                // Whether setContextClassLoader turns out to be necessary
//                // or not, we fail fast if permission is not available.
//                sm.checkPermission(new RuntimePermission("setContextClassLoader"));
//            }
//            this.task = task;
//            this.acc = AccessController.getContext();
//            this.ccl = Thread.currentThread().getContextClassLoader();
//        }
//
//        public T call() throws Exception {
//            try {
//                return AccessController.doPrivileged(
//                    new PrivilegedExceptionAction<T>() {
//                        public T run() throws Exception {
//                            Thread t = Thread.currentThread();
//                            ClassLoader cl = t.getContextClassLoader();
//                            if (ccl == cl) {
//                                return task.call();
//                            } else {
//                                t.setContextClassLoader(ccl);
//                                try {
//                                    return task.call();
//                                } finally {
//                                    t.setContextClassLoader(cl);
//                                }
//                            }
//                        }
//                    }, acc);
//            } catch (PrivilegedActionException e) {
//                throw e.getException();
//            }
//        }
//    }
//
//    /**线程池使用的默认的线程工厂
//     *
//     */
//    static class DefaultThreadFactory implements ThreadFactory {
//        //线程池的名字,可能有多个线程池。
//        private static final AtomicInteger poolNumber = new AtomicInteger(1);
//        private final ThreadGroup group;
//        //线程的名字
//        private final AtomicInteger threadNumber = new AtomicInteger(1);
//        private final String namePrefix;
//
//        DefaultThreadFactory() {
//            SecurityManager s = System.getSecurityManager();
//            group = (s != null) ? s.getThreadGroup() :
//                                  Thread.currentThread().getThreadGroup();
//            //线程名称
//            namePrefix = "pool-" +
//                          poolNumber.getAndIncrement() +
//                         "-thread-";
//        }
//
//        public Thread newThread(Runnable r) {
//            Thread t = new Thread(group, r,
//                                  namePrefix + threadNumber.getAndIncrement(),
//                                  0);
//            if (t.isDaemon())
//                //设置为非守护线程。
//                t.setDaemon(false);
//            if (t.getPriority() != Thread.NORM_PRIORITY)
//                t.setPriority(Thread.NORM_PRIORITY);
//            return t;
//        }
//    }
//
//    /**
//     * Thread factory capturing access control context and class loader
//     */
//    static class PrivilegedThreadFactory extends DefaultThreadFactory {
//        private final AccessControlContext acc;
//        private final ClassLoader ccl;
//
//        PrivilegedThreadFactory() {
//            super();
//            SecurityManager sm = System.getSecurityManager();
//            if (sm != null) {
//                // Calls to getContextClassLoader from this class
//                // never trigger a security check, but we check
//                // whether our callers have this permission anyways.
//                sm.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
//
//                // Fail fast
//                sm.checkPermission(new RuntimePermission("setContextClassLoader"));
//            }
//            this.acc = AccessController.getContext();
//            this.ccl = Thread.currentThread().getContextClassLoader();
//        }
//
//        public Thread newThread(final Runnable r) {
//            return super.newThread(new Runnable() {
//                public void run() {
//                    AccessController.doPrivileged(new PrivilegedAction<Void>() {
//                        public Void run() {
//                            Thread.currentThread().setContextClassLoader(ccl);
//                            r.run();
//                            return null;
//                        }
//                    }, acc);
//                }
//            });
//        }
//    }
//
//    /**
//     * A wrapper class that exposes only the ExecutorService methods
//     * of an ExecutorService implementation.
//     */
//    static class DelegatedExecutorService extends AbstractExecutorService {
//        private final java.util.concurrent.ExecutorService e;
//        DelegatedExecutorService(java.util.concurrent.ExecutorService executor) { e = executor; }
//        public void execute(Runnable command) { e.execute(command); }
//        public void shutdown() { e.shutdown(); }
//        public List<Runnable> shutdownNow() { return e.shutdownNow(); }
//        public boolean isShutdown() { return e.isShutdown(); }
//        public boolean isTerminated() { return e.isTerminated(); }
//        public boolean awaitTermination(long timeout, TimeUnit unit)
//            throws InterruptedException {
//            return e.awaitTermination(timeout, unit);
//        }
//        public Future<?> submit(Runnable task) {
//            return e.submit(task);
//        }
//        public <T> Future<T> submit(Callable<T> task) {
//            return e.submit(task);
//        }
//        public <T> Future<T> submit(Runnable task, T result) {
//            return e.submit(task, result);
//        }
//        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
//            throws InterruptedException {
//            return e.invokeAll(tasks);
//        }
//        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
//                                             long timeout, TimeUnit unit)
//            throws InterruptedException {
//            return e.invokeAll(tasks, timeout, unit);
//        }
//        public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
//            throws InterruptedException, ExecutionException {
//            return e.invokeAny(tasks);
//        }
//        public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
//                               long timeout, TimeUnit unit)
//            throws InterruptedException, ExecutionException, TimeoutException {
//            return e.invokeAny(tasks, timeout, unit);
//        }
//    }
//
//    static class FinalizableDelegatedExecutorService
//        extends DelegatedExecutorService {
//        FinalizableDelegatedExecutorService(ExecutorService executor) {
//            super(executor);
//        }
//        protected void finalize() {
//            super.shutdown();
//        }
//    }
//
//    /**
//     * A wrapper class that exposes only the ScheduledExecutorService
//     * methods of a ScheduledExecutorService implementation.
//     */
//    static class DelegatedScheduledExecutorService
//            extends DelegatedExecutorService
//            implements ScheduledExecutorService {
//        private final ScheduledExecutorService e;
//        DelegatedScheduledExecutorService(ScheduledExecutorService executor) {
//            super(executor);
//            e = executor;
//        }
//        public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
//            return e.schedule(command, delay, unit);
//        }
//        public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
//            return e.schedule(callable, delay, unit);
//        }
//        public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
//            return e.scheduleAtFixedRate(command, initialDelay, period, unit);
//        }
//        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
//            return e.scheduleWithFixedDelay(command, initialDelay, delay, unit);
//        }
//    }
//
//    /** Cannot instantiate. */
//    private Executors() {}
//}
