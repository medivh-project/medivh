package tech.medivh.core.jfr

import jdk.jfr.consumer.RecordedEvent
import jdk.jfr.consumer.RecordingFile
import tech.medivh.core.statistic.TestCaseRecordAccumulator
import tech.medivh.core.statistic.TestCaseRecord
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
                accumulator.computeIfAbsent(event.getString("testCase")) { TestCaseRecordAccumulator(it) }
                    .accumulate(event.thread.javaName, EventNode.fromMethodInvokeEvent(event))
            }
        }
        return accumulator.values.map { it.buildRecord() }.toList()
    }


    private val threadEventListMap = mutableMapOf<String, MutableList<EventNode>>()

    fun append(threadName: String, eventNode: EventNode) {
        threadEventListMap.computeIfAbsent(threadName) { mutableListOf() }.add(eventNode)
    }

}
