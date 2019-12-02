package com.seanghay.outstagram.egl.concurrency

import android.os.Looper
import java.util.concurrent.CountDownLatch

open class DispatchQueue : Thread {

    private lateinit var handler: DispatchQueueHandler
    protected val latchSync: CountDownLatch = CountDownLatch(1)

    constructor() : super()
    constructor(name: String) : super(name)

    init {
        this.start()
    }

    override fun run() {
        Looper.prepare()
        handler = DispatchQueueHandler(this)
        latchSync.countDown()
        Looper.loop()
        onBeforeQuit()
    }

    protected open fun onBeforeQuit() {}

    fun postRunnable(runnable: Runnable) {
        try {
            latchSync.await()
            handler.post(runnable)
        } catch (ie: InterruptedException) {
        }
    }

    fun removeRunnable(runnable: Runnable) {
        try {
            latchSync.await()
            handler.removeCallbacks(runnable)
        } catch (ie: InterruptedException) {
        }
    }

    fun removeRunnablesAndMessages() {
        try {
            latchSync.await()
            handler.removeCallbacksAndMessages(null)
        } catch (ie: InterruptedException) {
        }
    }

    fun getHandler(): DispatchQueueHandler? {
        try {
            latchSync.await()
            return handler
        } catch (ie: InterruptedException) {
        }

        return null
    }

    fun post(runnable: () -> Unit) {
        postRunnable(Runnable(runnable))
    }

    fun quit() {
        try {
            latchSync.await()
            handler.looper.quit()
            join()
        } catch (ie: InterruptedException) {
        }
    }
}