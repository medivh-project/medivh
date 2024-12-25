package tech.medivh.core.jfr

import org.slf4j.LoggerFactory
import java.io.RandomAccessFile
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.time.Duration
import java.time.Instant


/**
 *
 * A SortedSegment represents an ordered segment with an external sort
 * It maintains a linked list.
 * The first element is the start of the segment.
 *
 *
 *
 * @param start the start offset of the segment
 * @param end the segment end offset
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class SortedSegment(start: Long, private val end: Long, val file: RandomAccessFile) {
    private val batch = 16 * 1024
    private var currentOffset = start
    private val nodeArray = Array<SegmentNode?>(2048) { null }

    init {
        read()
    }

    fun first(): SegmentNode {
        return nodeArray[0]!!
    }

    private fun read() {
        nodeArray.fill(null)
        if (currentOffset >= end) {
            return
        }
        val bytes = ByteArray(currentReadLength())
        file.seek(currentOffset)
        file.read(bytes)
        LOGGER.info("read bytes from $currentOffset to ${currentOffset + bytes.size}")
        var arrayIndex = 0
        val buffer = ByteBuffer.wrap(bytes)
        var bufferOffset = 0
        while (arrayIndex < nodeArray.size && buffer.hasRemaining()) {
            try {
                val startTime = buffer.readInstant()
                val endTime = buffer.readInstant()
                val name = buffer.readLengthAndString()
                val className = buffer.readLengthAndString()
                val event = FlameNode(startTime, endTime, Duration.between(startTime, endTime), name, className)
                nodeArray[arrayIndex] = SegmentNode(arrayIndex, event)
                arrayIndex++
                bufferOffset = buffer.position()
            } catch (e: BufferUnderflowException) {
                //  ignore under flow lick TCP Fragmentation
                break
            }
        }
        currentOffset += bufferOffset
    }

    private fun currentReadLength(): Int {
        if (currentOffset + batch > end) {
            return (end - currentOffset).toInt()
        }
        return batch
    }


    private fun ByteBuffer.readInstant(): Instant {
        val instantLong = getLong()
        val epochSecond = instantLong / 1_000_000_000
        val nanoAdjustment = instantLong % 1_000_000_000
        return Instant.ofEpochSecond(epochSecond, nanoAdjustment)
    }

    private fun ByteBuffer.readLengthAndString(): String {
        val stringBytes = ByteArray(getShort().toInt() and 0xFFFF)
        get(stringBytes)
        return String(stringBytes, Charsets.UTF_8)
    }


    inner class SegmentNode(private val index: Int, val event: FlameNode) : Comparable<SegmentNode> {

        fun next(): SegmentNode? {
            if (index == nodeArray.size - 1 || nodeArray[index + 1] == null) {
                read()
                return nodeArray[0]
            }
            return nodeArray[index + 1]
        }

        override fun compareTo(other: SegmentNode): Int {
            return event.compareTo(other.event)
        }


    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SortedSegment::class.java)
    }


}
