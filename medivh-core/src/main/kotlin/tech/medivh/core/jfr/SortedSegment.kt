//package tech.medivh.core.jfr
//
//import java.io.RandomAccessFile
//import java.nio.BufferUnderflowException
//import java.nio.ByteBuffer
//import java.time.Instant
//
//
///**
// *
// * A SortedSegment represents an ordered segment with an external sort
// * It maintains a linked list.
// * The first element is the start of the segment.
// *
// *
// *
// * @param start the start offset of the segment
// * @param batch the batch size of read each the segment
// * @author gxz gongxuanzhangmelt@gmail.com
// **/
//class SortedSegment(start: Long, private val batch: Int, val maxCount: Int, val file: RandomAccessFile) {
//    var readCount = 0
//    var currentOffset = start
//    val nodeArray = Array<BlockingNode?>(batch) { null }
//
//    fun read(): BlockingNode {
//        val bytes = ByteArray(currentReadLength())
//        file.seek(currentOffset)
//        file.read(bytes)
//
//        var arrayIndex = 0
//        val buffer = ByteBuffer.wrap(bytes)
//        while (buffer.hasRemaining()) {
//            try {
//                val startTime = buffer.getLong().deserialize()
//                val endTime = buffer.getLong().deserialize()
//                val event = EventNode(buffer.getLong().deserialize(), buffer.getLong().deserialize(), buffer.int, buffer)
//                buffer.getLong()
//            } catch (e: BufferUnderflowException) {
//                //  ignore under flow
//                break
//            }
//        }
//
//        val node = BlockingNode()
//        return node
//    }
//
//    private fun currentReadLength(): Int {
//        return if (currentOffset + batch >= file.length()) {
//            (file.length() - currentOffset).toInt()
//        } else {
//            batch
//        }
//    }
//
//
//    private fun Long.deserialize(): Instant {
//        val epochSecond = this / 1_000_000_000
//        val nanoAdjustment = (this % 1_000_000_000).toInt()
//        return Instant.ofEpochSecond(epochSecond, nanoAdjustment.toLong())
//    }
//
//
////    class BlockingNode(private val index: Int, val event: EventNode) {
////        fun next(): BlockingNode {
////            if () {
////
////            }
////        }
////    }
//
//}
