package com.seanghay.outstagram.egl.concurrency

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference


class DispatchQueueHandler(dispatchQueue: DispatchQueue) : Handler() {

    private val dispatchQueueRef = WeakReference(dispatchQueue)

    override fun handleMessage(msg: Message) {

    }
}