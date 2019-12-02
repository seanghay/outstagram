package com.seanghay.outstagram.egl.core


class EglOffscreenSurface(
    eglCore: EglCore,
    private val width: Int,
    private val height: Int
) : EglSurface(eglCore) {

    override fun create() {
        createOffscreenSurface(width, height)
    }

    override fun release() {
        releaseEglSurface()
    }
}