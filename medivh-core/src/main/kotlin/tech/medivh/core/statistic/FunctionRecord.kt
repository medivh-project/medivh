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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FunctionRecord

        if (value != other.value) return false
        if (count != other.count) return false
        if (name != other.name) return false
        if (className != other.className) return false
        if (children != other.children) return false

        return true
    }

    override fun hashCode(): Int {
        var result = count.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + className.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + children.hashCode()
        return result
    }


}
