package tech.medivh.core.jfr

import jdk.jfr.consumer.RecordedEvent
import jdk.jfr.consumer.RecordingFile
import tech.medivh.core.statistic.TestCaseRecord
import tech.medivh.core.statistic.TestCaseRecordAccumulator
import java.io.File


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JfrEventClassifier(private val jfrFile: File) {


    fun classify(): List<TestCaseRecord> {
        val accumulator = mutableMapOf<String, TestCaseRecordAccumulator>()

        RecordingFile(jfrFile.toPath()).use { recordingFile ->
            while (recordingFile.hasMoreEvents()) {
                val event: RecordedEvent = recordingFile.readEvent()
                if (event.eventType.name != MethodInvokeEvent::class.java.name) {
                    continue
                }
                val testCase = event.getString("testCase")
                val threadName = event.thread.javaName
                val node = FlameNode.fromMethodInvokeEvent(event)
                accumulator.computeIfAbsent(testCase) { TestCaseRecordAccumulator(it) }.accumulate(threadName, node)
            }
        }
        return accumulator.values.map { it.buildRecord() }.toList()
    }


    private val threadEventListMap = mutableMapOf<String, MutableList<FlameNode>>()

    fun append(threadName: String, flameNode: FlameNode) {
        threadEventListMap.computeIfAbsent(threadName) { mutableListOf() }.add(flameNode)
    }

}
