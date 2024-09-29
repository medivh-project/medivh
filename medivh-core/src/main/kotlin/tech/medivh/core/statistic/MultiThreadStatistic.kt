package tech.medivh.core.statistic

import tech.medivh.core.InvokeInfo


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class MultiThreadStatistic(val methodName: String) {
    private val threadInvokeInfo = mutableMapOf<String, InvokeInfo>()
    var invokeCount: Long = 0
    var totalCost: Long = 0
    var maxCost: Long = 0
    var minCost: Long = 0


    val avgCost: Double
        get() {
            return totalCost.toDouble() / invokeCount
        }

    val threadInvoke: List<ThreadInvokeInfo>
        get() {
            return threadInvokeInfo.map { (threadName, invokeInfo) ->
                ThreadInvokeInfo(threadName, invokeInfo)
            }
        }


    fun merge(threadName: String, other: InvokeInfo) {
        threadInvokeInfo.merge(threadName, other) { old, new ->
            old.invokeCount += new.invokeCount
            old.totalCost += new.totalCost
            old.maxCost = old.maxCost.coerceAtLeast(new.maxCost)
            old.minCost = old.minCost.coerceAtMost(new.minCost)
            old
        }
        invokeCount += other.invokeCount
        totalCost += other.totalCost
        maxCost = maxCost.coerceAtLeast(other.maxCost)
        minCost = minCost.coerceAtMost(other.minCost)
    }
}

data class ThreadInvokeInfo(val threadName: String, val invokeInfo: InvokeInfo)
