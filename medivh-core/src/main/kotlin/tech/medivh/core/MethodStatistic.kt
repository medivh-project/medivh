package tech.medivh.core


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
data class MethodStatistic(
    val method: String,
    var invokeCount: Long,
    var totalCost: Long,
    var maxCost: Long,
    var minCost: Long
) {

    fun avgCost(): Long {
        return totalCost / invokeCount
    }
}
