package com.seanghay.outstagram.egl.gles.uniform

import android.opengl.GLES20

class FloatUniform(name: String) : Uniform<Float>(name) {
    override var value: Float
        get() {
            return if (cachedValue == null) {
                val args = FloatArray(1)
                GLES20.glGetUniformfv(program, location, args, 0)
                args[0]
            } else cachedValue!!
        }
        set(value) {
            GLES20.glUniform1f(location, value)
            cachedValue = value
        }
}