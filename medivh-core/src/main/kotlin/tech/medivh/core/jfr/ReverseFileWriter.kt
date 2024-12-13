package tech.medivh.core.jfr

import org.slf4j.LoggerFactory
import java.io.File
import java.nio.ByteBuffer
import java.time.Instant


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ReverseFileWriter(dir: File, jfrThread: JfrThread, bufferCapacity: Int = 1024 * 1024) {

    private val buffer = ByteBuffer.allocate(bufferCapacity)

    private val dataFile = dir.resolve("${jfrThread.javaThreadId}.rjfr")

    private val indexFile = dir.resolve("${jfrThread.javaThreadId}.ijfr")

    private var preIndex = 0L

    fun append(node: EventNode) {
        val nodeArray = node.serialize()
        if (buffer.remaining() < nodeArray.size) {
            flush()
        }
        buffer.put(nodeArray)
    }

    fun flush() {
        buffer.flip()
        val allByteArrays = ByteArray(buffer.remaining())
        buffer[allByteArrays]
        dataFile.appendBytes(allByteArrays)
        indexFile.appendBytes(ByteBuffer.allocate(Long.SIZE_BYTES).putLong(preIndex).array())
        log.info("flush ${dataFile.name} ${allByteArrays.size}")
        log.info("flush ${indexFile.name} $preIndex")
        preIndex += allByteArrays.size
        buffer.clear()
    }


    private fun EventNode.serialize(): ByteArray {
        val nameByte = name.toByteArray(Charsets.UTF_8)
        ByteBuffer.allocate(Long.SIZE_BYTES * 2 + Int.SIZE_BYTES + nameByte.size).apply {
            putLong(startTime.serialize())
            putLong(endTime.serialize())
            putInt(nameByte.size)
            put(nameByte)
            return array()
        }
    }


    private fun Instant.serialize(): Long {
        return this.epochSecond * 1_000_000_000 + this.nano.toLong()
    }

    companion object {
        private val log = LoggerFactory.getLogger(ReverseFileWriter::class.java)
    }

}
