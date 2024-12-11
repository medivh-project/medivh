package tech.medivh.core.jfr

import jdk.jfr.consumer.RecordedEvent
import java.time.Duration
import java.time.Instant


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JfrRecord(event: RecordedEvent) {
    val startTime: Instant = event.startTime
    val duration: Duration = event.duration
    val thread: JfrThread = JfrThread(event.thread)
    val method: JfrMethod = JfrMethod(event.stackTrace.frames[0].method)
    val stackTrace: JfrStackTrace = JfrStackTrace()

    override fun toString(): String {
        return "JfrRecord(startTime=$startTime, duration=$duration, thread=$thread, method=$method, stackTrace=$stackTrace)"
    }


}
