package tech.medivh.core.jfr

import jdk.jfr.consumer.RecordedEvent
import java.time.Duration
import java.time.Instant


/**
 *
 * a method invoke stack flame
 *
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class FlameNode(val startTime: Instant, val duration: Duration, val name: String, val className: String) :
    Comparable<FlameNode> {

    val children: MutableList<FlameNode> = mutableListOf()


    override fun compareTo(other: FlameNode): Int {
        if (startTime == other.startTime) {
            return -duration.compareTo(other.duration)
        }
        if (startTime.isBefore(other.startTime)) {
            return -1
        }
        return 1
    }

    override fun toString(): String {
        return "Node(startTime=$startTime, duration = $duration, name='$name', children=${children.size})"
    }

    companion object {


        fun fromMethodInvokeEvent(event: RecordedEvent): FlameNode {
            val invokeMethod = event.stackTrace.frames.first().method
            return FlameNode(event.startTime, event.duration, invokeMethod.name, invokeMethod.type.name)
        }

    }
}

