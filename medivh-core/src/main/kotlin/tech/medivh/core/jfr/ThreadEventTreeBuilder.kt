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
     * @param record must be in order by start time asc
     */
    fun processRecord(record: JfrRecord) {
        validateRecord(record)

        val newNode = EventNode.fromRecord(record)
        while (stack.isNotEmpty() && stack.last().startTime >= newNode.endTime) {
            stack.removeAt(stack.size - 1) // 弹出不包含当前节点的节点
        }
        stack.last().children.add(newNode)
        stack.add(newNode)
    }

    private fun validateRecord(record: JfrRecord) {
        check(record.thread == jfrThread)
        check(!preStartTime.isAfter(record.startTime))
        preStartTime = record.startTime
    }

    /**
     * return dummy root node
     */
    fun getTree(): EventNode = root

}
