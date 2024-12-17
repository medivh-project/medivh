package tech.medivh.core.jfr

import tech.medivh.core.InvokeInfo
import java.time.Instant


/**
 *
 * build a method tree from jfr event.
 * jfr event stream is order by start time in same thread.
 * but jfr order by start time desc.
 * so build tree O(N*h) h is tree height.
 * we should reverse the order of jfr event.
 *
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ThreadEventTreeBuilder(private val threadName: String) {

    private val root = EventNode(startTime = Instant.MIN, endTime = Instant.MAX, "")
    private var preStartTime = Instant.MIN
    private var minStartTime = Instant.MAX
    private var maxEndTime = Instant.MIN
    private var invokeInfoMap: MutableMap<String, InvokeInfo> = mutableMapOf()

    /**
     * process a jfr record
     * @param node must be in order by start time asc
     */
    fun processNode(node: EventNode) {
        validateRecord(node)
        accumulate(node)
        processNode(node, root)
    }

    private fun accumulate(node: EventNode) {
        //   duration unit
        invokeInfoMap.merge(node.name, InvokeInfo(node.duration().toMillis()), InvokeInfo::merge)
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

    private fun validateRecord(node: EventNode) {
        check(!preStartTime.isAfter(node.startTime)) {
            "jfr event is not order by start time"
        }
        preStartTime = node.startTime
        if (node.startTime < minStartTime) {
            minStartTime = node.startTime
        }
        if (node.endTime > maxEndTime) {
            maxEndTime = node.endTime
        }
    }

    /**
     * return dummy root node
     */
    fun getTree(): EventNode {
        val dummy = EventNode(startTime = minStartTime, endTime = maxEndTime, "all")
        dummy.children.addAll(root.children)
        return dummy
    }

    fun getStatistics(): Map<String, InvokeInfo> {
        return invokeInfoMap
    }

}
