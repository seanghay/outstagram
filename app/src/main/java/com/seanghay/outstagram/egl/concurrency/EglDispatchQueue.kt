package com.seanghay.outstagram.egl.concurrency

import com.seanghay.outstagram.egl.core.EglCore

class EglDispatchQueue : DispatchQueue("EglDispatchQueue") {

    private val _eglCore = EglCore()

    val eglCore: EglCore
        get() {
            latchSync.await()
            return _eglCore
        }

    init {
        post(eglCore::setup)
    }

    override fun onBeforeQuit() {
        eglCore.release()
    }
}
