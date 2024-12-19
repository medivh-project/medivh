package tech.medivh.core.serialize

import com.alibaba.fastjson2.JSONWriter
import com.alibaba.fastjson2.writer.ObjectWriter
import java.lang.reflect.Type
import java.time.Duration
import java.util.UUID


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class DurationSerializer : ObjectWriter<Duration> {

    override fun write(jsonWriter: JSONWriter?, `object`: Any?, fieldName: Any?, fieldType: Type?, features: Long) {
        `object` as Duration
        jsonWriter?.writeInt64(durationToLong(`object`))
        jsonWriter?.writeName("id")
        jsonWriter?.writeColon()
        jsonWriter?.writeString(UUID.randomUUID().toString())
    }


    companion object {
        fun durationToLong(duration: Duration): Long {
            return duration.seconds * 1_000_000_000 + duration.nano.toLong()
        }
    }
}