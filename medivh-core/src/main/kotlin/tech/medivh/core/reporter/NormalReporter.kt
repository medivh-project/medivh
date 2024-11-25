package tech.medivh.core.reporter

import com.alibaba.fastjson2.JSONArray
import tech.medivh.core.InvokeInfo
import tech.medivh.core.MethodToken
import tech.medivh.core.statistic.NormalStatistic
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object NormalReporter : AbstractTimeReport() {


    private val normalThreadFactory = ThreadFactory { r ->
        Thread(r, "Medivh-normal")
    }

    private val threadPool = ThreadPoolExecutor(
        1, 1, 0, TimeUnit.SECONDS,
        ArrayBlockingQueue(8196 * 4), normalThreadFactory
    )

    private val statisticMap = HashMap<String, InvokeInfo>(8196)

    override fun report(executeInfo: ExecuteInfo) {
        threadPool.submit {
            val token = MethodToken.fromExecuteInfo(executeInfo)
            statisticMap.merge(token, InvokeInfo(executeInfo.cost), InvokeInfo::merge)
        }
    }

    override fun htmlTemplateName(): String {
        return "report.html"
    }

    override fun generateJsonString(): String {
        val result = JSONArray()
        statisticMap.forEach { (methodToken, invokeInfo) ->
            val statistic = NormalStatistic(methodToken)
            statistic.invokeCount = invokeInfo.invokeCount
            statistic.totalCost = invokeInfo.totalCost
            statistic.maxCost = invokeInfo.maxCost
            statistic.minCost = invokeInfo.minCost
            statistic.expectTime = methodSetupMap[methodToken]?.expectTime ?: 0
            result.add(statistic)
        }
        return result.toJSONString()
    }

}
