package tech.medivh.core.jfr

import jdk.jfr.consumer.RecordedEvent
import jdk.jfr.consumer.RecordingFile
import tech.medivh.core.statistic.TestCaseRecord
import java.io.File


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JfrEventClassifier(private val jfrFile: File) {


    fun classify(): List<TestCaseRecord> {
        val testCaseRecordMap = mutableMapOf<String, TestCaseRecord>()
        RecordingFile(jfrFile.toPath()).use { recordingFile ->
            while (recordingFile.hasMoreEvents()) {
                val event: RecordedEvent = recordingFile.readEvent()
                if (event.eventType.name != MethodInvokeEvent::class.java.name) {
                    continue
                }
                val testCaseRecord = testCaseRecordMap.computeIfAbsent(event.getString("testCase")) { TestCaseRecord(it) }
                testCaseRecord.append(event.thread.javaName, EventNode.fromMethodInvokeEvent(event))
            }
        }
        return testCaseRecordMap.values.toList()
    }
}
