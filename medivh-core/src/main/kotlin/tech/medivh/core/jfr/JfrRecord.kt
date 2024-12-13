package tech.medivh.core.jfr

import jdk.jfr.consumer.RecordedEvent
import java.time.Duration
import java.time.Instant


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JfrRecord(event: RecordedEvent) : Comparable<JfrRecord> {
    val startTime: Instant = event.startTime
    val endTime: Instant = event.endTime
    val duration: Duration = event.duration
    val thread: JfrThread = JfrThread(event.thread)
    val method: JfrMethod = JfrMethod(event.stackTrace.frames[0].method)
    val stackTrace: JfrStackTrace = JfrStackTrace()

    override fun compareTo(other: JfrRecord): Int {
        if(startTime == other.startTime){
            return duration.compareTo(other.duration)
        }
        if (startTime.isBefore(other.startTime)) {
            return -1
        }
        return 1
    }

    override fun toString(): String {
        return "JfrRecord(startTime=$startTime, endTime=$endTime, thread=$thread, method=$method, stackTrace=$stackTrace)"
    }


}
