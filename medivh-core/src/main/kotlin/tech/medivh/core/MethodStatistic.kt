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

    //  todo How to do make a agent jar contain a third-party library
    fun jsonLine(): String {
        return "{\"method\":\"$method\",\"invokeCount\":$invokeCount,\"totalCost\":$totalCost,\"maxCost\":$maxCost,\"minCost\":$minCost,\"avgCost\":${avgCost()}}"
    }
}
