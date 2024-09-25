package tech.medivh.core

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object TimeReporter {

    private val threadPool = ThreadPoolExecutor(
        1, 1,
        0, TimeUnit.SECONDS,
        ArrayBlockingQueue(8196 * 4),
        MedivhThreadFactory
    )

    //  todo  init capacity by scan method count 
    private val startMap = HashMap<String, MethodStartRecord>(8196)

    private val statisticMap = HashMap<String, MethodStatistic>(8196)

    fun start(token: String) {
        threadPool.submit {
            startMap[token] = MethodStartRecord(token)
        }
    }

    fun end(token: String) {
        threadPool.submit {
            startMap.remove(token)?.let {
                val cost = System.currentTimeMillis() - it.startTime
                val methodStatistic = MethodStatistic(token, 1, cost, cost, cost)
                statisticMap.merge(token, methodStatistic) { old, new ->
                    old.invokeCount += new.invokeCount
                    old.totalCost += new.totalCost
                    old.maxCost = old.maxCost.coerceAtLeast(new.maxCost)
                    old.minCost = old.minCost.coerceAtMost(new.minCost)
                    old
                }
            }
        }
    }


    fun generateHtml(): String {
        this.javaClass.classLoader.getResourceAsStream("report/report.html")?.use {
            return it.readBytes().toString(Charsets.UTF_8)
        } ?: return ""
    }

}


object MedivhThreadFactory : ThreadFactory {

    override fun newThread(r: Runnable): Thread {
        return Thread(r, "Medivh-Thread")
    }

}
