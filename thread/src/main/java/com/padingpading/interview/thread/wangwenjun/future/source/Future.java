/*
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

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.padingpading.interview.thread.wangwenjun.future.source;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**异步计算的结果
 * 1、判断异步任务是否完成。
 * 2、等待异步任务的结果。
 * 3、获取异步任务的结果。
 */
public interface Future<V> {

    /**取消任务
     */
    boolean cancel(boolean mayInterruptIfRunning);

    /**任务是否被取消。
     */
    boolean isCancelled();

    /**
     */
    boolean isDone();

    /**返回future异步执行任务的结果
     * 如果任务还没有完成,get方法进入阻塞状态。
     *
     *
     */
    V get() throws InterruptedException, ExecutionException;

    /**可超时返回future异步执行任务的结果
     * 超时抛出TimeoutException。
     */
    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}
