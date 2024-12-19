package tech.medivh.core.statistic

import tech.medivh.core.InvokeInfo
import tech.medivh.core.jfr.FlameNode
import java.time.Duration
import java.time.Instant


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ThreadRecordAccumulator(val name: String) {
    private val eventList = mutableListOf<FlameNode>()
    private val statistic = mutableMapOf<String, InvokeInfo>()

    private var minStartTime = Instant.MAX
    private var maxDuration = Duration.ZERO

    fun accumulate(event: FlameNode) {
        statistic.merge(event.name, InvokeInfo(event.duration.toMillis()), InvokeInfo::merge)
        eventList.add(event)
        if (event.startTime < minStartTime) {
            minStartTime = event.startTime
        }
        if (event.duration > maxDuration) {
            maxDuration = event.duration
        }
    }

    fun buildRecord(): ThreadRecord {
        eventList.sort()
        val root = FlameNode(minStartTime, maxDuration, "all", "medivh")
        eventList.forEach {
            processNode(it, root)
        }

        val mergedChildren = mergeChildren(root.children)
        var rootDuration = Duration.ZERO
        mergedChildren.forEach {
            rootDuration += it.value
        }
        val rootRecord = FunctionRecord(root.name, root.className, rootDuration)
        rootRecord.children.addAll(mergedChildren)
        return ThreadRecord(name).apply { functionRoot = rootRecord }
    }


    private fun mergeChildren(children: List<FlameNode>): List<FunctionRecord> {
        if (children.isEmpty()) {
            return emptyList()
        }
        if (children.size == 1) {
            val flame = children.first()
            return listOf(FunctionRecord(flame.name, flame.className, flame.duration))
        }
        return children.groupBy { it.name + it.className }.values.map { mergeSameNodes(it) }.toList()
    }

    private fun mergeSameNodes(sameNodes: List<FlameNode>): FunctionRecord {
        var duration = Duration.ZERO
        var count = 0
        val allFlameChildren = mutableListOf<FlameNode>()
        sameNodes.forEach {
            duration += it.duration
            count++
            allFlameChildren.addAll(it.children)
        }

        val recordChildren = mergeChildren(allFlameChildren)
        return FunctionRecord(sameNodes.first().name, sameNodes.first().className, duration, count).apply {
            children.addAll(recordChildren)
        }
    }


    private fun processNode(node: FlameNode, parent: FlameNode) {
        val searchNodeParentNode = parent.children.binarySearch {
            if (it.startTime.isAfter(node.startTime)) {
                1
            } else if (it.startTime.isBefore(node.startTime)) {
                -1
            } else {
                -node.duration.compareTo(it.duration)
            }
        }
        if (searchNodeParentNode < 0) {
            parent.children.add(node)
            return
        }
        processNode(node, parent.children[searchNodeParentNode])
    }

}
