package tech.medivh.core.jfr

import org.slf4j.LoggerFactory
import java.io.BufferedOutputStream
import java.io.File
import java.nio.ByteBuffer


/**
 *
 *
 * we should use external sort to sort the jfr records
 * after sorting, we can build the tree in O(N) time complexity.
 * we will have a file to store the sorted records. (threadName.me)
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ExternalSortedWriter(dir: File, jfrThread: JfrThread, private val count: Int = 64 * 1024) {
    init {
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    private val dataList = ArrayList<FlameNode>(count)

    private val dataFile = dir.resolve("${jfrThread.javaName}.rjfr")

    private val indexFile = dir.resolve("${jfrThread.javaName}.ijfr")

    private val index = mutableListOf<Long>()

    private var preIndex = 0L

    private var allow = true

    fun append(node: FlameNode) {
        require(allow) { "this writer has been closed" }
        dataList.add(node)
        if (dataList.size == count) {
            internalFlush()
        }
    }

    /**
     * invoke this method means all data has been written.
     */
    fun flush() {
        require(allow) { "this writer has been closed" }
        internalFlush()
        allow = false
        val indexBytes = ByteBuffer.allocate(index.size * Long.SIZE_BYTES).apply {
            index.forEach { putLong(it) }
        }
        indexFile.writeBytes(indexBytes.array())
    }


    private fun internalFlush() {
        if (dataList.isEmpty()) {
            return
        }
        dataList.sort()
        var currentLength = 0
        BufferedOutputStream(dataFile.outputStream()).use { dataWriter ->
            dataList.forEach {
                val bytes = it.serialize()
                dataWriter.write(bytes)
                currentLength += bytes.size
            }
        }
        index.add(preIndex)
        preIndex += currentLength
        log.info("flush ${dataFile.name} $currentLength bytes")
        dataList.clear()
    }


    /**
     * use 48 bits to store duration, we can store 2^48 / 1_000_000_000(ns) = 281474s about 78h
     */
    private fun FlameNode.serialize(): ByteArray {
        val nameByte = name.toByteArray(Charsets.UTF_8)
        val classNameByte = className.toByteArray(Charsets.UTF_8)
        // string split to short length + string bytes
        ByteBuffer.allocate(Short.SIZE_BYTES * 2 + Long.SIZE_BYTES * 2 + nameByte.size + classNameByte.size).apply {
            putLong(startTime.epochSecond * 1_000_000_000 + startTime.nano.toLong())
            putLong(endTime.epochSecond * 1_000_000_000 + endTime.nano.toLong())

            putShort(nameByte.size.toShort())
            put(nameByte)

            putShort(classNameByte.size.toShort())
            put(classNameByte)

            return array()
        }
    }


    companion object {
        private val log = LoggerFactory.getLogger(ExternalSortedWriter::class.java)
    }


}
