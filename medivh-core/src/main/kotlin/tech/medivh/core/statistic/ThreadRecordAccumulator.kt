package tech.medivh.core.statistic

import tech.medivh.core.InvokeInfo
import tech.medivh.core.jfr.EventNode
import java.time.Instant


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ThreadRecordAccumulator(val name: String) {
    private val eventList = mutableListOf<EventNode>()
    private val statistic = mutableMapOf<String, InvokeInfo>()

    private var minStartTime = Instant.MAX
    private var maxEndTime = Instant.MIN

    fun accumulate(event: EventNode) {
        statistic.merge(event.name, InvokeInfo(event.duration().toMillis()), InvokeInfo::merge)
        eventList.add(event)
        if (event.startTime < minStartTime) {
            minStartTime = event.startTime
        }
        if (event.endTime > maxEndTime) {
            maxEndTime = event.endTime
        }
    }

    fun buildRecord(): ThreadRecord {
        eventList.sort()
        val root = EventNode(minStartTime, maxEndTime, "all")
        eventList.forEach {
            processNode(it, root)
        }
        return ThreadRecord(name).apply { functionRoot = root }
    }

    private fun processNode(node: EventNode, parent: EventNode) {
        val searchNodeParentNode = parent.children.binarySearch {
            if (it.startTime.isAfter(node.startTime)) {
                return@binarySearch 1
            } else if (it.endTime.isBefore(node.endTime)) {
                return@binarySearch -1
            } else {
                0
            }
        }
        if (searchNodeParentNode < 0) {
            parent.children.add(node)
            return
        }
        processNode(node, parent.children[searchNodeParentNode])
    }

}
