package tech.medivh.core.statistic



/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class NormalStatistic(val methodName: String) {

    var invokeCount: Long = 0
    var totalCost: Long = 0
    var maxCost: Long = 0
    var minCost: Long = 0
    val avgCost: Double
        get() {
            return totalCost.toDouble() / invokeCount
        }


}
