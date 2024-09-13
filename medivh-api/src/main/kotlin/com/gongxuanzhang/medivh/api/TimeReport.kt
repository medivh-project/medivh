package com.gongxuanzhang.medivh.api

import java.util.concurrent.TimeUnit
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.milliseconds

/**
 * @author gongxuanzhangmelt@gmail.com
 */
object TimeReport  {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    fun <T> timeReport(actionName: String, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> T): T {
        return when (timeUnit) {
            TimeUnit.MICROSECONDS, TimeUnit.NANOSECONDS -> {
                val startTime = System.nanoTime()
                val result = action.invoke()
                val nano = System.nanoTime() - startTime
                log.info("finish [$actionName] for ${formatDuration(nano, timeUnit)}")
                result
            }

            else -> {
                val startTime = System.currentTimeMillis()
                val result = action.invoke()
                val nano = (System.currentTimeMillis() - startTime).milliseconds.inWholeNanoseconds
                log.info("finish [$actionName] for ${formatDuration(nano, timeUnit)}")
                result
            }
        }
    }


    private fun formatDuration(nanoseconds: Long, unit: TimeUnit): String {
        val nanosPerSecond = 1_000_000_000L
        val nanosPerMillisecond = 1_000_000L
        val nanosPerMinute = 60 * nanosPerSecond
        val nanosPerHour = 60 * nanosPerMinute

        val convertedNs = when (unit) {
            TimeUnit.NANOSECONDS -> nanoseconds
            TimeUnit.MICROSECONDS -> nanoseconds * 1000
            TimeUnit.MILLISECONDS -> nanoseconds * nanosPerMillisecond
            TimeUnit.SECONDS -> nanoseconds * nanosPerSecond
            TimeUnit.MINUTES -> nanoseconds * nanosPerMinute
            TimeUnit.HOURS -> nanoseconds * nanosPerHour
            TimeUnit.DAYS -> nanoseconds * nanosPerHour * 24
        }

        val hours = convertedNs / nanosPerHour
        val minutes = (convertedNs % nanosPerHour) / nanosPerMinute
        val seconds = (convertedNs % nanosPerMinute) / nanosPerSecond
        val milliseconds = (convertedNs % nanosPerSecond) / nanosPerMillisecond
        val microseconds = (convertedNs % nanosPerMillisecond) / 1000
        val nano = convertedNs % 1000

        return StringBuilder().apply {
            append(hours).append("h ")
            append(minutes).append("m ")
            append(seconds).append("s ")
            append(milliseconds).append("ms ")
            append(microseconds).append("μs ")
            append(nano).append("ns")
        }.toString()
    }
}
