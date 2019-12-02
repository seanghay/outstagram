package com.seanghay.outstagram.egl.gles.uniform

import android.opengl.GLES20

abstract class Uniform<T>(val name: String) {

    var program: Int = -1
        private set

    var location: Int = -1
        private set

    protected var cachedValue: T? = null
    abstract var value: T

    fun create(program: Int) {
        this.program = program
        GLES20.glUseProgram(program)
        location = GLES20.glGetUniformLocation(program, name)
    }

}