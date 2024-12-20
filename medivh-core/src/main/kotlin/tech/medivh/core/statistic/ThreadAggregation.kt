package tech.medivh.core.statistic

import com.alibaba.fastjson2.annotation.JSONField
import tech.medivh.core.InvokeInfo
import tech.medivh.core.jfr.FlameNode
import tech.medivh.core.serialize.DurationSerializer
import java.time.Duration
import java.time.Instant


/**
 *
 *
 * aggregation of all record in a thread
 * contains:
 *  - The earliest execution time
 *  - The latest execution time
 *  - The longest execution time of all records
 *
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ThreadAggregation(
    @JSONField(serialize = false)
    var earliest: Instant = Instant.MAX,
    @JSONField(serialize = false)
    var latest: Instant = Instant.MIN,
    @JSONField(serialize = false)
    var maxDuration: Duration = Duration.ZERO
) {

    /**
     * assign id , start from 2 , 1 is root
     */
    private var id = 2L

    private val idMap: MutableMap<String, Long> = mutableMapOf()

    val threadTotalInvoke: MutableMap<Long, InvokeInfo> = mutableMapOf()

    @get:JSONField(serializeUsing = DurationSerializer::class)
    val duration
        get() = Duration.between(earliest, latest)

    fun gather(event: FlameNode) {
        if (event.startTime < earliest) {
            earliest = event.startTime
        }
        if (event.duration > maxDuration) {
            maxDuration = event.duration
        }
        if (event.endTime > latest) {
            latest = event.endTime
        }
        threadTotalInvoke.merge(
            assignId(event),
            InvokeInfo(
                DurationSerializer.durationToLong(event.duration),
                methodName = event.name,
                className = event.className
            ),
            InvokeInfo::merge
        )
    }

    fun assignId(event: FlameNode): Long {
        return idMap.computeIfAbsent("${event.className}#${event.name}") { id++ }
    }
}
