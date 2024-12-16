package tech.medivh.core.jfr

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
class ThreadEventTreeBuilder(private val jfrThread: JfrThread) {

    private val root = EventNode(startTime = Instant.MIN, endTime = Instant.MAX, "")
    private val stack = mutableListOf(root)
    private var preStartTime = Instant.MIN

    /**
     * process a jfr record
     * @param node must be in order by start time asc
     */
    fun processNode(node: EventNode) {
        validateRecord(node)

        while (stack.isNotEmpty() && stack.last().startTime >= node.endTime) {
            stack.removeAt(stack.size - 1)
        }
        stack.last().children.add(node)
        stack.add(node)
    }

    private fun validateRecord(node: EventNode) {
        check(!preStartTime.isAfter(node.startTime))
        preStartTime = node.startTime
    }

    /**
     * return dummy root node
     */
    fun getTree(): EventNode = root

}
