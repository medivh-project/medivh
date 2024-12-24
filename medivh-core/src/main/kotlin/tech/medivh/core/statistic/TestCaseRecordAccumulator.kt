package tech.medivh.core.statistic

import tech.medivh.core.jfr.FlameNode


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class TestCaseRecordAccumulator(val name: String) {

    /**
     * key is thread name
     */
    private val threadRecordAccumulatorMutableMap = mutableMapOf<String, ThreadRecordAccumulator>()

    fun accumulate(threadName: String, event: FlameNode) {
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
