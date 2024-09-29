package tech.medivh.core.reporter

import com.alibaba.fastjson2.JSONArray
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import tech.medivh.core.InvokeInfo
import tech.medivh.core.MethodStartRecord
import tech.medivh.core.statistic.MultiThreadStatistic
import tech.medivh.core.unexpectedly


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object MultiThreadTimeReporter : TimeReporter {

    private val threadPool = ThreadPoolExecutor(
        1, 1, 0, TimeUnit.SECONDS,
        ArrayBlockingQueue(8196 * 4)
    ) { r ->
        Thread(r, "Medivh-multi-thread")
    }


    private val startMap = HashMap<String, HashMap<String, MutableList<MethodStartRecord>>>(8196)

    private val statisticMap = HashMap<String, HashMap<String, InvokeInfo>>(8196)

    override fun start(token: String) {
        val threadName = Thread.currentThread().name
        threadPool.submit {
            val threadMap = startMap.computeIfAbsent(token) { HashMap() }
            threadMap[threadName] = ArrayList<MethodStartRecord>().apply { add(MethodStartRecord(token)) }
        }
    }

    override fun end(token: String) {
        val threadName = Thread.currentThread().name
        threadPool.submit {
            val methodList = startMap[token]?.get(threadName) ?: unexpectedly()
            methodList.removeLast().let {
                val cost = System.currentTimeMillis() - it.startTime
                val invokeInfo = InvokeInfo(1, cost, cost, cost)
                statisticMap.computeIfAbsent(token) { HashMap() }.merge(threadName, invokeInfo) { old, new ->
                    old.merge(new)
                    old
                }
            }
        }
    }

    override fun htmlTemplateName(): String {
        return "report-mt.html"
    }

    override fun generateJsonString(): String {
        val jsonArray = JSONArray()
        println(statisticMap)
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


