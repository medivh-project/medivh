package tech.medivh.core


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
data class InvokeInfo(
    var totalCost: Long,
    var invokeCount: Long = 1,
    var maxCost: Long = totalCost,
    var minCost: Long = totalCost,
    val methodName: String = "",
    val className: String = ""
) {

    companion object {
        /**
         * This function modifies the original value
         */
        fun merge(x: InvokeInfo, y: InvokeInfo): InvokeInfo {

            return x.apply {
                totalCost += y.totalCost
                invokeCount += y.invokeCount
                maxCost = maxCost.coerceAtLeast(y.maxCost)
                minCost = minCost.coerceAtMost(y.minCost)
            }
        }
    }
}
