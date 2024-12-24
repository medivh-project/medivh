package tech.medivh.core.statistic

import tech.medivh.core.jfr.FlameNode
import java.time.Duration


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ThreadRecordAccumulator(val name: String) {
    private val eventList = mutableListOf<FlameNode>()

    private val aggregation = ThreadAggregation()

    fun accumulate(event: FlameNode) {
        eventList.add(event)
        aggregation.gather(event)
    }

    fun buildRecord(sort: Boolean = true): ThreadRecord {
        if (sort) {
            eventList.sort()
        }
        val root = FlameNode(
            aggregation.earliest,
            aggregation.latest,
            Duration.between(aggregation.earliest, aggregation.latest),
            "all",
            "medivh"
        )
        eventList.forEach {
            processNode(it, root)
        }

        val mergedChildren = mergeChildren(root.children)
        var rootDuration = Duration.ZERO
        mergedChildren.forEach {
            rootDuration += it.value
        }
        val rootRecord = FunctionRecord(root.name, root.className, rootDuration, 1, null, 1)
        rootRecord.addChildren(mergedChildren)
        return ThreadRecord(name, aggregation).apply {
            functionRoot = rootRecord
        }
    }


    private fun mergeChildren(children: List<FlameNode>): List<FunctionRecord> {
        if (children.isEmpty()) {
            return emptyList()
        }
        if (children.size == 1) {
            val flame = children.first()
            return listOf(
                FunctionRecord(
                    flame.name,
                    flame.className,
                    flame.duration,
                    aggregation.assignId(flame),
                    null,
                    1
                )
            )
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

        return FunctionRecord(
            sameNodes.first().name,
            sameNodes.first().className,
            duration,
            aggregation.assignId(sameNodes.first()),
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
