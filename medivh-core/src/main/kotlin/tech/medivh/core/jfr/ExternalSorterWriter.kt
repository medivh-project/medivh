package tech.medivh.core.jfr

import org.slf4j.LoggerFactory
import java.io.BufferedOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.time.Instant


/**
 *
 *
 * we should use external sort to sort the jfr records
 * after sorting, we can build the tree in O(N) time complexity.
 * we will have a file to store the sorted records. (threadName.me)
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ExternalSorterWriter(dir: File, jfrThread: JfrThread, testCase: JfrMethod, private val count: Int = 64 * 1024) {
    init {
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    private val dataList = ArrayList<EventNode>(count)

    private val dataFile = dir.resolve("${jfrThread.javaThreadId}.rjfr")

    private val index = mutableListOf<Long>()

    private var preIndex = 0L

    private var allow = true

    fun append(node: EventNode) {
        require(allow) { "this writer has been closed" }
        dataList.add(node)
        if (dataList.size == count) {
            flush()
        }
    }

    /**
     * invoke this method means all data has been written.
     */
    fun flush() {
        require(allow) { "this writer has been closed" }
        internalFlush()
        allow = false
        mergeSortAndWrite()
    }

    private fun mergeSortAndWrite() {
//        val blockingList: MutableList<Blocking> = mutableListOf()
//        val queue = PriorityQueue<EventNode>()
    }

    private fun internalFlush() {
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
        log.info("flush ${dataFile.name} $currentLength bytes")
        dataList.clear()
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
        private val log = LoggerFactory.getLogger(ExternalSorterWriter::class.java)
    }


}
