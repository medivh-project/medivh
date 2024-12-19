package tech.medivh.core.statistic

import com.alibaba.fastjson2.annotation.JSONField
import tech.medivh.core.serialize.DurationSerializer
import java.time.Duration


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class FunctionRecord(
    val name: String,
    val className: String,
    @JSONField(serializeUsing = DurationSerializer::class)
    var value: Duration,
    var count: Int = 1
) {
    val children = mutableListOf<FunctionRecord>()
}
