package tech.medivh.core.statistic


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ThreadRecord(val name: String, val aggregation: ThreadAggregation) {
    var functionRoot: FunctionRecord? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ThreadRecord

        if (name != other.name) return false
        if (aggregation != other.aggregation) return false
        if (functionRoot != other.functionRoot) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + aggregation.hashCode()
        result = 31 * result + (functionRoot?.hashCode() ?: 0)
        return result
    }


}
