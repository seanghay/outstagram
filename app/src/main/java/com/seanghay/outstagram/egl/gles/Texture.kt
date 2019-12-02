package com.seanghay.outstagram.egl.gles

abstract class Texture {

    abstract fun create()
    abstract fun release()
    abstract fun configure()
    abstract fun enable()
    abstract fun disable()

    inline fun use(block: Texture.() -> Unit) {
        enable()
        block()
        disable()
    }
}