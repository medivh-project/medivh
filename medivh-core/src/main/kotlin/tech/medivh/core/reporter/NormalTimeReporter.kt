package tech.medivh.core.reporter

import com.alibaba.fastjson2.JSONArray
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import tech.medivh.core.InvokeInfo
import tech.medivh.core.MethodStartRecord
import tech.medivh.core.statistic.NormalStatistic


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object NormalTimeReporter : TimeReporter {

    private val threadPool = ThreadPoolExecutor(
        1, 1, 0, TimeUnit.SECONDS,
        ArrayBlockingQueue(8196 * 4)
    ) { r ->
        Thread(r, "Medivh-normal")
    }


    private val startMap = HashMap<String, MethodStartRecord>(8196)

    private val statisticMap = HashMap<String, InvokeInfo>(8196)

    override fun start(token: String) {
        threadPool.submit {
            startMap[token] = MethodStartRecord(token)
        }
    }

    override fun end(token: String) {
        threadPool.submit {
            startMap.remove(token)?.let {
                val cost = System.currentTimeMillis() - it.startTime
                val invokeInfo = InvokeInfo(1, cost, cost, cost)
                statisticMap.merge(token, invokeInfo) { old, new ->
                    old.merge(new)
                    old
                }
            }
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


