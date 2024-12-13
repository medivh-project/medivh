package tech.medivh.core.jfr

import jdk.jfr.consumer.RecordedEvent
import jdk.jfr.consumer.RecordingFile
import java.io.File
import java.nio.file.Paths


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JfrAnalyzer {

    fun analysis(jfrFile: File, consumer: (JfrRecord) -> Unit) {
        RecordingFile(Paths.get(jfrFile.toURI())).use { recordingFile ->
            while (recordingFile.hasMoreEvents()) {
                val event: RecordedEvent = recordingFile.readEvent()
                if (event.eventType.name == MethodInvokeEvent::class.java.name) {
                    consumer.invoke(JfrRecord(event))
                }
            }
        }
    }


}

