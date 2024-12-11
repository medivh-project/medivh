package tech.medivh.core.jfr

import com.alibaba.fastjson2.JSONObject
import jdk.jfr.ValueDescriptor
import jdk.jfr.consumer.RecordedEvent
import jdk.jfr.consumer.RecordingFile
import org.junit.jupiter.api.Test
import java.nio.file.Paths


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JfrAnalysisTest {

    @Test
    fun analysis() {
        val uri = this.javaClass.classLoader.getResource("test-recording.jfr")?.toURI() ?: return
        // 打开 JFR 文件并读取事件
        var count = 0
        RecordingFile(Paths.get(uri)).use { recordingFile ->
            while (recordingFile.hasMoreEvents()) {
                val event: RecordedEvent = recordingFile.readEvent()
                val duration = event.duration
                if (event.eventType.name == MethodInvokeEvent::class.java.name) {
//                    event.thread.fields.forEach {
//                        val a: Any = event.thread.getValue(it.name)
//                        println(a)
//                    }
                    println(
                    )
                    a(event.stackTrace.fields)
                    return
                }
            }
        }
        println(count)
    }

    fun a(fields: List<ValueDescriptor>) {
        val json = JSONObject()
        fields.forEach {
            json[it.name] = it.typeName
            println("${it.name} : ${it.typeName},")
        }
    }
}
