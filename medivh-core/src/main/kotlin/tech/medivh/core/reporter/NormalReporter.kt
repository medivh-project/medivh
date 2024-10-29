package tech.medivh.core.reporter

import com.alibaba.fastjson2.JSONArray
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import tech.medivh.core.InvokeInfo
import tech.medivh.core.statistic.NormalStatistic


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object NormalReporter : TimeReporter {


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
            val token = "${executeInfo.invokeClassName}#${executeInfo.methodName}"
            statisticMap.merge(token, InvokeInfo(executeInfo.cost), InvokeInfo::merge)
        }
    }

    override fun htmlTemplateName(): String {
        return "report.html"
    }

    override fun generateJsonString(): String {
        val result = JSONArray()
        statisticMap.forEach { (methodName, invokeInfo) ->
            val statistic = NormalStatistic(methodName)
            statistic.invokeCount = invokeInfo.invokeCount
            statistic.totalCost = invokeInfo.totalCost
            statistic.maxCost = invokeInfo.maxCost
            statistic.minCost = invokeInfo.minCost
            result.add(statistic)
        }
        return result.toJSONString()
    }

}
