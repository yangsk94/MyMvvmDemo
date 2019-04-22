package com.utils


import java.util.Observable
import java.util.TimerTask

/**
 * Created by yangsk on 2017/9/18.
 */

class TimeEventEmitter : Observable {

    private var mTimerTask: TimerTask? = null

    //只要创建大于0 就会执行定时刷新
    private var mInterval: Int = 1000

    constructor() {}

    constructor(interval: Int) {
        mInterval = interval
    }

    fun startTimerTask() {
        if (mTimerTask == null) {
            mTimerTask = EmitterSchedule()
        } else {
            Logger.i("roomMsg", "------->startTimerTask() your timer already on the run")
            return
        }
        try {
            TaskEngine.instance?.schedule(
                mTimerTask as EmitterSchedule, (if (mInterval > 0) mInterval else INTERVAL).toLong(),
                (if (mInterval > 0) mInterval else INTERVAL).toLong()
            )
        } catch (ex: IllegalStateException) {
            Logger.i("roomMsg", "--------> timer still can be canceled， timer start failed")
            cancelEmit()
            startTimerTask()
        }

    }

    fun cancelEmit() {
        if (null != mTimerTask) {
            TaskEngine.instance?.cancelScheduledTask(mTimerTask)
            mTimerTask = null
        }
    }

    fun terminateEmitter() {
        cancelEmit()
        deleteObservers()
    }

    /**
     * notify observers what happened
     *
     * @param arg
     */
    fun sendEventManually(arg: Any) {
        setChanged()
        //send a heart beat
        notifyObservers(arg)
    }

    private inner class EmitterSchedule : TimerTask() {

        override fun run() {
            //work thread
            setChanged()
            //send a heart beat
            notifyObservers()
        }
    }

    companion object {

        private val INTERVAL = 1000 * 1//1s 一次

        fun emit(): TimeEventEmitter {
            val eventEmitter = TimeEventEmitter()
            eventEmitter.startTimerTask()
            return eventEmitter
        }

        fun emit(interval: Int): TimeEventEmitter {
            val eventEmitter = TimeEventEmitter(interval)
            eventEmitter.startTimerTask()
            return eventEmitter
        }
    }
}
