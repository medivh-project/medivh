package tech.medivh.core.statistic

import tech.medivh.core.jfr.EventNode


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class TestCaseRecordAccumulator(val name: String) {

    private val threadRecordAccumulatorMutableMap = mutableMapOf<String, ThreadRecordAccumulator>()

    fun accumulate(threadName: String, event: EventNode) {
        threadRecordAccumulatorMutableMap.computeIfAbsent(threadName) {
            ThreadRecordAccumulator(it)
        }.accumulate(event)
    }

    fun buildRecord(): TestCaseRecord {
        return TestCaseRecord(name).apply {
            threadRecordAccumulatorMutableMap.values.forEach { threads.add(it.buildRecord()) }
        }
    }
}
