package com.gongxuanzhang.medivh.core

import java.io.File
import java.nio.ByteBuffer


/**
 * save file
 */
//  todo
class MedivhCache(private val cacheFile: File) {

    val lastTime: Long

    init {
        if (!cacheFile.exists()) {
            lastTime = 0
        } else {
            val buffer = ByteBuffer.wrap(cacheFile.readBytes())
            lastTime = buffer.long
        }

    }

    fun save() {
        val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
        buffer.putLong(System.currentTimeMillis())
        cacheFile.writeBytes(buffer.array())
    }

}
