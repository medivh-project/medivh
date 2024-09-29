package tech.medivh.core


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
data class InvokeInfo(
    var invokeCount: Long,
    var totalCost: Long,
    var maxCost: Long,
    var minCost: Long
) {
    fun merge(other: InvokeInfo) {
        this.invokeCount += other.invokeCount
        this.totalCost += other.totalCost
        this.maxCost = this.maxCost.coerceAtLeast(other.maxCost)
        this.minCost = this.minCost.coerceAtMost(other.minCost)
    }
}
