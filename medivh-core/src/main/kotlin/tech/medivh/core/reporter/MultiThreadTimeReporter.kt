package tech.medivh.core.reporter

import com.alibaba.fastjson2.JSONArray
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import tech.medivh.core.InvokeInfo
import tech.medivh.core.statistic.MultiThreadStatistic


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object MultiThreadTimeReporter : TimeReporter {

    private val multiModeThreadFactory = ThreadFactory { r ->
        Thread(r, "Medivh-multi-thread")
    }

    private val threadPool = ThreadPoolExecutor(
        1, 1, 0, TimeUnit.SECONDS,
        ArrayBlockingQueue(8196 * 4), multiModeThreadFactory
    )


    private val statisticMap = HashMap<String, HashMap<String, InvokeInfo>>(8196)

    override fun report(executeInfo: ExecuteInfo) {
        threadPool.submit {
            val token = "${executeInfo.invokeClassName}#${executeInfo.methodName}"
            val threadMap = statisticMap.computeIfAbsent(token) { HashMap() }
            threadMap.merge(executeInfo.threadName!!, InvokeInfo(executeInfo.cost), InvokeInfo::merge)
        }
    }


    override fun htmlTemplateName(): String {
        return "report-mt.html"
    }

    override fun generateJsonString(): String {
        val jsonArray = JSONArray()
        statisticMap.forEach { (methodName, threadMap) ->
            val statistic = MultiThreadStatistic(methodName)
            threadMap.forEach { (threadName, invokeInfo) ->
                statistic.merge(threadName, invokeInfo)
            }
            jsonArray.add(statistic)
        }
        return jsonArray.toJSONString()
    }


}


