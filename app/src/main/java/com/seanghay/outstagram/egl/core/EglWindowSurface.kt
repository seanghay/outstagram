package com.seanghay.outstagram.egl.core

import android.graphics.SurfaceTexture
import android.view.Surface


class EglWindowSurface : EglSurface {

    private val surface: Any
    private var releaseSurface = false

    constructor(
        eglCore: EglCore,
        surface: Surface,
        releaseSurface: Boolean
    ) : super(eglCore) {
        this.releaseSurface = releaseSurface
        this.surface = surface
    }

    constructor(eglCore: EglCore, surfaceTexture: SurfaceTexture) :
            super(eglCore) {
        surface = surfaceTexture
    }

    fun recreate(newEglCore: EglCore) {
        eglCore = newEglCore
        create()
    }

    override fun create() {
        createWindowSurface(surface)
    }

    override fun release() {
        releaseEglSurface()
        if (releaseSurface && surface is Surface)
            surface.release()
    }

}