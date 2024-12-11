package tech.medivh.core.statistic

import tech.medivh.core.InvokeInfo
import tech.medivh.core.jfr.JfrRecord

/**
 * @author gongxuanzhangmelt@gmail.com
 */
class JfrStatistic(record: JfrRecord) {

    var invokeCount: Long = 1
    var totalCost: Long = record.duration.toMillis()
    var maxCost: Long = record.duration.toMillis()
    var minCost: Long = record.duration.toMillis()
    var expectTime: Long = 0

    private val threadInvokeInfo = mutableMapOf<String, InvokeInfo>()


    companion object {
        fun merge(a: JfrStatistic, b: JfrStatistic): JfrStatistic {
            a.invokeCount += b.invokeCount
            a.totalCost += b.totalCost
            a.maxCost = maxOf(a.maxCost, b.maxCost)
            a.minCost = minOf(a.minCost, b.minCost)
            b.threadInvokeInfo.forEach { k, v ->
                a.threadInvokeInfo.merge(k, v, InvokeInfo::merge)
            }
            return a
        }
    }


}