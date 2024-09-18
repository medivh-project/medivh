package com.gongxuanzhang.medivh.api

import com.gongxuanzhang.medivh.api.TimeReport.formatTime
import java.time.Duration
import java.util.concurrent.TimeUnit
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test


class TimeReportTest {


    @Test
    fun testHoursMinutesSeconds() {
        val duration = Duration.ofHours(1).plusMinutes(2).plusSeconds(5)
        val result = formatTime(duration.toNanos(), TimeUnit.SECONDS)
        assertEquals("1h 2min 5s", result)
    }

    @Test
    fun testMinutesAndSeconds() {
        val duration = Duration.ofMinutes(1).plusSeconds(5)
        val result = formatTime(duration.toNanos(), TimeUnit.SECONDS)
        assertEquals("1min 5s", result)
    }

    @Test
    fun testSecondsMillisecondsMicrosecondsNanoseconds() {
        val duration = Duration.ofSeconds(1).plusMillis(1).plusNanos(500_001)
        val result = formatTime(duration.toNanos(), TimeUnit.MICROSECONDS)
        assertEquals("1s 1ms 500us", result)
    }

    @Test
    fun testNanoseconds() {
        val duration = Duration.ofNanos(1500)
        val result = formatTime(duration.toNanos(), TimeUnit.NANOSECONDS)
        assertEquals("1us 500ns", result)
    }

    @Test
    fun testOnlySeconds() {
        val duration = Duration.ofSeconds(1)
        val result = formatTime(duration.toNanos(), TimeUnit.MILLISECONDS)
        assertEquals("1s", result)
    }

    @Test
    fun testZeroNanoseconds() {
        val duration = Duration.ofNanos(0)
        val result = formatTime(duration.toNanos(), TimeUnit.NANOSECONDS)
        assertEquals("0ns", result)
    }

    @Test
    fun testLargeNumber() {
        val duration = Duration.ofHours(27).plusMinutes(46).plusSeconds(40)
        val result = formatTime(duration.toNanos(), TimeUnit.NANOSECONDS)
        assertEquals("27h 46min 40s", result)
    }

}
