package com.utils

import java.util.Date
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

class TaskEngine
/**
 * Constructs a new task engine.
 */
private constructor() {

    private var mTimer: Timer? = null

    private var mExecutors: ExecutorService? = null

    private var mWrappedTasks: MutableMap<TimerTask, TimerTaskWrapper>? = null

    val executorService: ExecutorService?
        get() {
            initParamsIfNecessary()
            return mExecutors
        }

    init {
        initParamsIfNecessary()
    }

    /**
     * Submits a Runnable task for execution and returns a Future representing
     * that task.
     *
     * @param task
     * the task to submit.
     * @return a Future representing pending completion of the task, and whose
     * <tt>get()</tt> method will return <tt>null</tt> upon completion.
     * @throws java.util.concurrent.RejectedExecutionException
     * if task cannot be scheduled for execution.
     * @throws NullPointerException
     * if task null.
     */
    fun submit(task: Runnable): Future<*>? {
        initParamsIfNecessary()
        return mExecutors?.submit(task)
    }

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the `Executor` implementation.
     *
     * @param task the runnable task
     * @throws java.util.concurrent.RejectedExecutionException if this task cannot be
     * accepted for execution
     * @throws NullPointerException if command is null
     */
    fun execute(task: Runnable) {
        initParamsIfNecessary()
        mExecutors?.execute(task)
    }

    /**
     * Schedules the specified task for execution after the specified delay.
     *
     * @param task
     * task to be scheduled.
     * @param delay
     * delay in milliseconds before task is to be executed.
     * @throws IllegalArgumentException
     * if <tt>delay</tt> is negative, or
     * <tt>delay + System.currentTimeMillis()</tt> is negative.
     * @throws IllegalStateException
     * if task was already scheduled or cancelled, or mTimer was
     * cancelled.
     */
    fun schedule(task: TimerTask, delay: Long) {
        initParamsIfNecessary()
        mTimer?.schedule(TimerTaskWrapper(task), delay)
    }

    /**
     * Schedules the specified task for execution at the specified time. If the
     * time is in the past, the task is scheduled for immediate execution.
     *
     * @param task
     * task to be scheduled.
     * @param time
     * time at which task is to be executed.
     * @throws IllegalArgumentException
     * if <tt>time.getTime()</tt> is negative.
     * @throws IllegalStateException
     * if task was already scheduled or cancelled, mTimer was
     * cancelled, or mTimer thread terminated.
     */
    fun schedule(task: TimerTask, time: Date) {
        initParamsIfNecessary()
        mTimer?.schedule(TimerTaskWrapper(task), time)
    }

    /**
     * Schedule a task for repeated fixed-delay execution after a specific delay.
     *
     * @param task the task to schedule.
     * @param delay amount of time in milliseconds before first execution.
     * @param period amount of time in milliseconds between subsequent executions.
     */
    fun schedule(task: TimerTask, delay: Long, period: Long) {
        initParamsIfNecessary()
        val taskWrapper = TimerTaskWrapper(task)
        mWrappedTasks?.set(task, taskWrapper)
        mTimer?.schedule(taskWrapper, delay, period)
    }

    /**
     * check a task is running or not
     *
     * @param task current running scheduled task.
     * @return true if the task is scheduled otherwise false
     */
    fun isTaskScheduled(task: TimerTask?): Boolean? {
        initParamsIfNecessary()
        return if (null == task) false else mWrappedTasks?.containsKey(task)
    }

    /**
     * Cancels the execution of a scheduled task.
     * [TimerTask.cancel]
     *
     * @param task
     * the scheduled task to cancel.
     */
    fun cancelScheduledTask(task: TimerTask?) {
        if (task != null) {
            initParamsIfNecessary()
            val taskWrapper = mWrappedTasks?.remove(task)
            taskWrapper?.cancel()
        }
    }

    /**
     * Shuts down the task engine service.
     */
    private fun shutdown() {
        mExecutors?.shutdownNow()
        mExecutors = null

        if (mTimer != null) {
            mTimer?.cancel()
            mTimer = null
        }
    }

    /**
     * init parameters in case of npe !!!
     */
    private fun initParamsIfNecessary() {
        if (null == mWrappedTasks) {
            mWrappedTasks = ConcurrentHashMap()
        }
        if (null == mTimer) {
            mTimer = Timer("xcbb-timer-spark", true)
        }
        if (null == mExecutors) {
            mExecutors = Executors.newCachedThreadPool(object : ThreadFactory {

                internal val threadNumber = AtomicInteger(1)

                override fun newThread(runnable: Runnable): Thread {
                    // Use our own naming scheme for the threads.
                    val thread = Thread(
                        Thread.currentThread().threadGroup,
                        runnable,
                        "xcbb-pool-spark" + threadNumber.getAndIncrement(),
                        0
                    )
                    // Make workers daemon threads.
                    thread.isDaemon = true
                    if (thread.priority != Thread.NORM_PRIORITY) {
                        thread.priority = Thread.NORM_PRIORITY
                    }
                    return thread
                }
            })
        }
    }

    /**
     * Wrapper class for a standard TimerTask. It simply executes the TimerTask
     * using the mExecutors's thread pool.
     */
    private inner class TimerTaskWrapper(private val task: TimerTask) : TimerTask() {

        override fun run() {
            mExecutors?.submit(task)
        }
    }

    companion object {

        private var INSTANCE: TaskEngine? = null

        /**
         * Returns a task engine INSTANCE (singleton).
         *
         * @return a task engine.
         */
        val instance: TaskEngine?
            get() {
                if (INSTANCE == null) {
                    synchronized(TaskEngine::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = TaskEngine()
                        }
                    }
                }
                return this.INSTANCE
            }
    }
}