package com.gongxuanzhang.medivh.api

import java.util.concurrent.TimeUnit

/**
 * @author gongxuanzhangmelt@gmail.com
 */
object TimeReport {

     fun <T> timeReport(
        actionName: String,
        timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
        console: Boolean = false,
        action: () -> T
    ): T {
        return when (timeUnit) {
            TimeUnit.MICROSECONDS, TimeUnit.NANOSECONDS -> {
                val startTime = System.nanoTime()
                val result = action.invoke()
                val nano = System.nanoTime() - startTime
                if (console) {
                    println("$actionName cost $nano ns")
                }
                result
            }

            else -> {
                val startTime = System.currentTimeMillis()
                val result = action.invoke()
                if (console) {
                    println("$actionName cost ${System.currentTimeMillis() - startTime} ms")
                }
                result
            }
        }
    }


    fun formatTime(nanoSeconds: Long, timeUnit: TimeUnit): String {
        val hours = TimeUnit.NANOSECONDS.toHours(nanoSeconds)
        val minutes = TimeUnit.NANOSECONDS.toMinutes(nanoSeconds) % 60
        val seconds = TimeUnit.NANOSECONDS.toSeconds(nanoSeconds) % 60
        val millis = TimeUnit.NANOSECONDS.toMillis(nanoSeconds) % 1000
        val micros = TimeUnit.NANOSECONDS.toMicros(nanoSeconds) % 1000
        val nanos = nanoSeconds % 1000

        val result = StringBuilder()

        if (hours > 0 && timeUnit <= TimeUnit.HOURS) result.append("${hours}h ")
        if (minutes > 0 && timeUnit <= TimeUnit.MINUTES) result.append("${minutes}min ")
        if (seconds > 0 && timeUnit <= TimeUnit.SECONDS) result.append("${seconds}s ")
        if (millis > 0 && timeUnit <= TimeUnit.MILLISECONDS) result.append("${millis}ms ")
        if (micros > 0 && timeUnit <= TimeUnit.MICROSECONDS) result.append("${micros}us ")
        if (nanos > 0 && timeUnit == TimeUnit.NANOSECONDS) result.append("${nanos}ns ")
        if (result.isEmpty()) {
            return when (timeUnit) {
                TimeUnit.SECONDS -> "0s"
                TimeUnit.MICROSECONDS -> "0us"
                TimeUnit.NANOSECONDS -> "0ns"
                else -> "0ms"
            }
        }
        return result.toString().trim()
    }


}
