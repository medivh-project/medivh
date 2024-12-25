package tech.medivh.core.jfr

import tech.medivh.core.statistic.ThreadRecord
import tech.medivh.core.statistic.ThreadRecordAccumulator
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.util.*


/**
 *
 *
 * @see ExternalClassifier
 * @see ExternalSortedWriter
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ExternalSortedReader(threadDir: File) {

    private val dataFile = threadDir.listFiles { file -> file.extension == "rjfr" }!!.first()

    private val indexFile = threadDir.listFiles { file -> file.extension == "ijfr" }!!.first()

    private val accumulator = ThreadRecordAccumulator(dataFile.nameWithoutExtension)

    fun read(): ThreadRecord {
        val allIndex = readAllIndex()
        val dataRandomAccessFile = RandomAccessFile(dataFile, "r")
        val nodes = allIndex.mapIndexed { index, offset ->
            val isLast = index == allIndex.size - 1
            val end = if (isLast) dataFile.length() else allIndex[index + 1]
            SortedSegment(offset, end, dataRandomAccessFile).first()
        }
        val priorityQueue = PriorityQueue(nodes)
        while (priorityQueue.isNotEmpty()) {
            val minNode = priorityQueue.poll()
            minNode.next()?.let { priorityQueue.offer(it) }
            accumulator.accumulate(minNode.event)
        }
        dataRandomAccessFile.close()
        return accumulator.buildRecord()
    }


    private fun readAllIndex(): List<Long> {
        ByteBuffer.wrap(indexFile.readBytes()).apply {
            val index = mutableListOf<Long>()
            while (hasRemaining()) {
                index.add(long)
            }
            return index
        }
    }

    override fun toString(): String {
        return "testCase:[${dataFile.parentFile.parentFile.name}] threadId:[${dataFile.nameWithoutExtension}]"
    }


}
