package com.seanghay.outstagram.egl.gles

import android.opengl.GLES20

data class TextureShader(
    var fragment: String = DEFAULT_FRAGMENT,
    var vertex: String = DEFAULT_VERTEX
) {

    private var fragmentHandle: Int = -1
    private var vertexHandle: Int = -1

    var program: Int = -1
        private set

    fun create() {

    }

    fun draw(texture: Texture) {
        GLES20.glUseProgram(program)

    }

    fun release() {

    }

    companion object {
        // language=glsl
        val DEFAULT_VERTEX =
            """
                uniform mat4 mvpMatrix;
                attribute vec4 position;
                void main() {
                    gl_Position = mvpMatrix * position; 
                }
            """.trimIndent()

        // language=glsl
        val DEFAULT_FRAGMENT =
            """
                precision mediump float;
                uniform vec4 color;
                void main() {    
                    gl_FragColor = color;
                }
            """.trimIndent()
    }
}