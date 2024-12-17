package tech.medivh.core.jfr

import jdk.jfr.consumer.RecordedEvent
import java.time.Duration
import java.time.Instant


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class EventNode(val startTime: Instant, val endTime: Instant, val name: String) : Comparable<EventNode> {

    val children: MutableList<EventNode> = mutableListOf()

    fun duration(): Duration {
        return Duration.between(startTime, endTime)
    }

    override fun compareTo(other: EventNode): Int {
        if (startTime == other.startTime) {
            return -endTime.compareTo(other.endTime)
        }
        if (startTime.isBefore(other.startTime)) {
            return -1
        }
        return 1
    }

    override fun toString(): String {
        return "Node(startTime=$startTime, endTime=$endTime, name='$name', children=${children.size})"
    }

    companion object {

        fun fromRecord(record: JfrRecord): EventNode {
            return EventNode(record.startTime, record.endTime, record.method.name)
        }

        fun fromMethodInvokeEvent(event: RecordedEvent): EventNode {
            return EventNode(event.startTime, event.endTime, event.stackTrace.frames.first().method.name)
        }

    }
}
