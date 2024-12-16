package tech.medivh.plugin.gradle.kotlin

import jdk.jfr.consumer.RecordedEvent
import jdk.jfr.consumer.RecordingFile
import org.junit.jupiter.api.Test
import tech.medivh.core.jfr.MethodInvokeEvent
import kotlin.io.path.toPath


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class AnalysisTest {


    @Test
    fun reverseTest() {
        val url = this.javaClass.classLoader.getResource("aaa.jfr").toURI()
        RecordingFile(url.toPath()).use { recordingFile ->
            while (recordingFile.hasMoreEvents()) {
                val event: RecordedEvent = recordingFile.readEvent()
                if (event.eventType.name == MethodInvokeEvent::class.java.name) {
                    println(event.getString("testCase"))
                }
            }
        }
    }

}
