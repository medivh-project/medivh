package tech.medivh.core.statistic

import tech.medivh.core.InvokeInfo
import tech.medivh.core.jfr.JfrRecord

/**
 * @author gongxuanzhangmelt@gmail.com
 */
class JfrStatistic(record: JfrRecord) {

    val methodName = "${record.method.className}#${record.method.name}"
    var invokeCount: Long = 1
    var totalCost: Long = record.duration.toMillis()
    var maxCost: Long = record.duration.toMillis()
    var minCost: Long = record.duration.toMillis()
    var expectTime: Long = 0

    // this map support merge
    private val threadInvokeMap = mutableMapOf(record.thread.key() to InvokeInfo(record.duration.toMillis()))

    val avgCost: Double
        get() {
            return totalCost.toDouble() / invokeCount
        }

    val threadInvoke: List<ThreadInvokeInfo>
        get() {
            return threadInvokeMap.map { (threadName, invokeInfo) ->
                ThreadInvokeInfo(threadName, invokeInfo)
            }
        }

    companion object {
        fun merge(a: JfrStatistic, b: JfrStatistic): JfrStatistic {
            a.invokeCount += b.invokeCount
            a.totalCost += b.totalCost
            a.maxCost = maxOf(a.maxCost, b.maxCost)
            a.minCost = minOf(a.minCost, b.minCost)
            b.threadInvokeMap.forEach { (k, v) ->
                a.threadInvokeMap.merge(k, v, InvokeInfo::merge)
            }
            return a
        }
    }


}


