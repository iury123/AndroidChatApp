package com.example.iurymiguel.androidchatapp.utils

import android.os.Handler

class TimeoutProvider {

    private val mHandler: Handler = Handler()
    private val mTimeout: Long = 10000
    private lateinit var mRunnable: Runnable

    /**
     * Starts a timer
     * @param callback to view after timeout.
     */
    fun startTimer(callback: () -> Unit) {
        mRunnable = Runnable {
            callback()
        }
        mHandler.postDelayed(mRunnable, mTimeout)
    }

    /**
     * Cancels timer.
     */
    fun cancelTimer() {
        mHandler.removeCallbacks(mRunnable)
    }
}