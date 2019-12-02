package com.seanghay.outstagram.egl.gles

import android.opengl.GLES20

class Texture2d(id: Int = NO_TEXTURE): Texture() {

    var id: Int = id
        private set

    private var previousId: Int = 0

    private fun tryCreate() {
        if (id != NO_TEXTURE) return
        val args = IntArray(1)
        GLES20.glGenTextures(args.size, args, 0)
        id = args[0]
    }

    private fun getCurrentTextureId(): Int {
        val args = IntArray(1)
        GLES20.glGetIntegerv(GLES20.GL_TEXTURE_BINDING_2D, args, 0)
        return args[0]
    }

    override fun create() {
        tryCreate()
    }

    override fun release() {
        if (id == NO_TEXTURE) return
        val args = intArrayOf(id)
        GLES20.glDeleteTextures(args.size, args, 0)
        id = NO_TEXTURE
    }

    override fun configure() {
        use {
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_NEAREST
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE
            )
        }
    }

    override fun enable() {
        tryCreate()
        previousId = getCurrentTextureId()
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id)
    }

    override fun disable() {
        if (id == NO_TEXTURE) return
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, previousId)
    }

    companion object {
        const val NO_TEXTURE = -1
    }
}