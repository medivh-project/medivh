package tech.medivh.core.statistic

import tech.medivh.core.jfr.EventNode
import tech.medivh.core.jfr.ThreadEventTreeBuilder


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class TestCaseRecord(val name: String) {

    private val threadEventListMap = mutableMapOf<String, MutableList<EventNode>>()

    fun append(threadName: String, eventNode: EventNode) {
        threadEventListMap.computeIfAbsent(threadName) { mutableListOf() }.add(eventNode)
    }

    fun buildTree(): Map<String, EventNode> {
        return threadEventListMap.map { (threadName, eventList) ->
            eventList.sort()
            val treeBuilder = ThreadEventTreeBuilder(threadName)
            eventList.forEach { treeBuilder.processNode(it) }
            //  todo
            println(treeBuilder.getStatistics())
            threadName to treeBuilder.getTree()
        }.toMap()
    }

}
