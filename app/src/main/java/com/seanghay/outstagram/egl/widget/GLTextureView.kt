package com.seanghay.outstagram.egl.widget

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.util.AttributeSet
import android.view.TextureView
import com.seanghay.outstagram.egl.concurrency.EglDispatchQueue
import com.seanghay.outstagram.egl.core.EglWindowSurface

class GLTextureView : TextureView, TextureView.SurfaceTextureListener {

    private val dispatchQueue: EglDispatchQueue = EglDispatchQueue()
    private var windowSurface: EglWindowSurface? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    init {
        create()
    }

    private fun create() {
        surfaceTextureListener = this
    }


    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        dispatchQueue.post {
            GLES20.glViewport(0, 0, width, height)
        }
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        dispatchQueue.post { windowSurface?.release() }
        dispatchQueue.quit()
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        surface ?: return

        dispatchQueue.post { windowSurface?.release() }
        windowSurface = EglWindowSurface(dispatchQueue.eglCore, surface)
        dispatchQueue.post {
            windowSurface?.create()
            makeCurrent()
        }

        drawFrame()
    }

    fun requestDraw() {
        drawFrame()
    }

    private fun drawFrame() = dispatchQueue.post {
        GLES20.glViewport(0, 0, width, height)
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        swapBuffers()
    }


    private fun swapBuffers() {
        windowSurface?.swapBuffers()
    }


    private fun makeCurrent() {
        windowSurface?.makeCurrent()
    }

}