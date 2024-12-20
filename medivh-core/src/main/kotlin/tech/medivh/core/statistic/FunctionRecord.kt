package tech.medivh.core.statistic

import com.alibaba.fastjson2.annotation.JSONField
import tech.medivh.core.InvokeInfo
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
    val globalInvokeInfo: InvokeInfo,
    var id: Long? = null,
    var parentId: Long? = null,
    var count: Int = 1
) {
    val children = mutableListOf<FunctionRecord>()

    fun addChildren(children: Collection<FunctionRecord>) {
        children.forEach { it.parentId = id }
        this.children.addAll(children)
    }

    fun addChildren(child: FunctionRecord) {
        child.parentId = id
        children.add(child)
    }
}
