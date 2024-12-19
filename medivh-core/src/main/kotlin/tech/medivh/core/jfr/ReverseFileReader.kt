package tech.medivh.core.jfr

import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.time.Instant


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ReverseFileReader(dir: File, jfrThread: JfrThread) {

    private val dataFile = dir.resolve("${jfrThread.javaThreadId}.rjfr")

    private val indexFile = dir.resolve("${jfrThread.javaThreadId}.ijfr")


    fun readOrderNode(action: (FlameNode) -> Unit) {
        TODO()
//        RandomAccessFile(dataFile, "r").use { file ->
//            var end = dataFile.length()
//            orderIndex().forEach { indexOffset ->
//                file.seek(indexOffset)
//                val bytes = ByteArray((end - indexOffset).toInt())
//                file.read(bytes)
//                val buffer = ByteBuffer.wrap(bytes)
//                while (buffer.hasRemaining()) {
//                    val startTime = buffer.getLong().deserialize()
//                    val endTime = buffer.getLong().deserialize()
//                    val nameBytes = ByteArray(buffer.getInt())
//                    buffer[nameBytes]
//                    val node = FlameNode(startTime, endTime, String(nameBytes))
//                    action(node)
//                }
//                end = indexOffset
//            }
//        }
    }


    private fun orderIndex(): List<Long> {
        val indexList = mutableListOf<Long>()
        val indexBuffer = ByteBuffer.wrap(indexFile.readBytes())
        while (indexBuffer.hasRemaining()) {
            indexList.add(indexBuffer.long)
        }
        return indexList.reversed()
    }

    private fun Long.deserialize(): Instant {
        val epochSecond = this / 1_000_000_000
        val nanoAdjustment = (this % 1_000_000_000).toInt()
        return Instant.ofEpochSecond(epochSecond, nanoAdjustment.toLong())
    }
}
