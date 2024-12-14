package tech.medivh.core.jfr

import java.io.File


/**
 *
 * jfr event order by start time and duration,but in descending order.
 * Descending order is very difficult for building trees.
 * time complexity is O(N*h) h is tree height.
 * so we should reverse the order of jfr event. time complexity is O(N).
 * but event may be so many, so we should use a file to store event.
 *
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JfrReverser(private val jfrFile: File) {

    private val threadSet = mutableSetOf<JfrThread>()

    fun writeReverse() {
        val dataFileMap = mutableMapOf<JfrThread, ReverseFileWriter>()
        JfrAnalyzer().analysis(jfrFile) { record ->
            val node = EventNode.fromRecord(record)
            dataFileMap.computeIfAbsent(record.thread) {
                threadSet.add(it)
                ReverseFileWriter(jfrFile.parentFile, it)
            }.append(node)
        }
        dataFileMap.values.forEach { it.flush() }
    }

    fun readReverse() {
        threadSet.forEach { thread ->
            ReverseFileReader(jfrFile.parentFile, thread).readOrderNode { node ->
                println(node)
            }
        }
    }


}



