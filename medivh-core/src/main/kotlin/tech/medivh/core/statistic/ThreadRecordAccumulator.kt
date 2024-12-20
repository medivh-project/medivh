package tech.medivh.core.statistic

import tech.medivh.core.InvokeInfo
import tech.medivh.core.jfr.FlameNode
import tech.medivh.core.serialize.DurationSerializer
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
    private var maxEndTime = Instant.MIN

    private var id = 0L

    private val globalStatistic = mutableMapOf<String, InvokeInfo>()

    fun accumulate(event: FlameNode) {
        statistic.merge(event.name, InvokeInfo(event.duration.toMillis()), InvokeInfo::merge)
        eventList.add(event)
        if (event.startTime < minStartTime) {
            minStartTime = event.startTime
        }
        if (event.duration > maxDuration) {
            maxDuration = event.duration
        }
        if (event.endTime > maxEndTime) {
            maxEndTime = event.endTime
        }

    }

    fun buildRecord(): ThreadRecord {
        eventList.sort()
        val root = FlameNode(minStartTime, maxEndTime, maxDuration, "all", "medivh")
        eventList.forEach {
            processNode(it, root)
            globalStatistic.merge(
                "${it.className}#${it.name}",
                InvokeInfo(DurationSerializer.durationToLong(it.duration)),
                InvokeInfo::merge
            )
        }

        val mergedChildren = mergeChildren(root.children)
        var rootDuration = Duration.ZERO
        mergedChildren.forEach {
            rootDuration += it.value
        }
        val rootInvokeInfo = InvokeInfo(DurationSerializer.durationToLong(rootDuration))
        val rootRecord = FunctionRecord(root.name, root.className, rootDuration, rootInvokeInfo, ++id, null, 1)
        rootRecord.addChildren(mergedChildren)
        return ThreadRecord(name).apply { functionRoot = rootRecord }
    }


    private fun mergeChildren(children: List<FlameNode>): List<FunctionRecord> {
        if (children.isEmpty()) {
            return emptyList()
        }
        if (children.size == 1) {
            val flame = children.first()
            val invokeInfo = globalStatistic["${flame.className}#${flame.name}"]!!
            return listOf(FunctionRecord(flame.name, flame.className, flame.duration, invokeInfo, ++id, null, 1))
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

        val invokeInfo = globalStatistic["${sameNodes.first().className}#${sameNodes.first().name}"]!!
        return FunctionRecord(
            sameNodes.first().name,
            sameNodes.first().className,
            duration,
            invokeInfo,
            ++id,
            null,
            count
        ).apply {
            addChildren(mergeChildren(allFlameChildren))
        }
    }


    private fun processNode(node: FlameNode, parent: FlameNode) {
        val searchNodeParentNode = parent.children.binarySearch {
            if (it.startTime.isAfter(node.startTime)) {
                1
            } else if (it.endTime.isBefore(node.endTime)) {
                -1
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
