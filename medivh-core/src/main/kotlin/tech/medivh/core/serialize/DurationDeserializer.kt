package tech.medivh.core.serialize

import com.alibaba.fastjson2.JSONReader
import com.alibaba.fastjson2.reader.ObjectReader
import java.lang.reflect.Type
import java.time.Duration


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class DurationDeserializer : ObjectReader<Duration> {

    override fun readObject(jsonReader: JSONReader?, fieldType: Type?, fieldName: Any?, features: Long): Duration {
        TODO("Not yet implemented")
    }


    companion object {
        fun longToDuration(long: Long): Duration {
            return Duration.ofSeconds(long / 1_000_000_000, long % 1_000_000_000)
        }
    }
}
