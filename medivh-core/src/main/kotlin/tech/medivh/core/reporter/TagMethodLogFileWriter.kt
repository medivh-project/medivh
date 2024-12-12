package tech.medivh.core.reporter

import net.bytebuddy.agent.builder.AgentBuilder
import java.io.File
import java.nio.ByteBuffer


/**
 *
 * after test task,java agent will write a log file wo record method info.
 * this action invoke in interceptor.
 * Performance losses caused by run-time reflection are avoided
 *
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class TagMethodLogFileWriter(file: File) {

    private val bufferedOutputStream = file.outputStream().buffered()

    /**
     * @see [AgentBuilder.Transformer.transform] params
     *
     */
    fun writeMethod(tagMethod: TagMethod) {
        tagMethod.className.toBufferBytes().let {
            bufferedOutputStream.write(it)
        }
        tagMethod.methodName.toBufferBytes().let {
            bufferedOutputStream.write(it)
        }
        tagMethod.expectTime.toBufferBytes().let {
            bufferedOutputStream.write(it)
        }
    }

    fun close() {
        bufferedOutputStream.close()
    }


    private fun String.toBufferBytes(): ByteArray {
        this.toByteArray().let {
            return ByteBuffer.allocate(Int.SIZE_BYTES + this.length).putInt(it.size).put(it).array()
        }
    }


    private fun Long.toBufferBytes(): ByteArray {
        return ByteArray(Long.SIZE_BYTES) { i -> (this shr (8 * (Long.SIZE_BYTES - 1 - i)) and 0xFF).toByte() }
    }

}
