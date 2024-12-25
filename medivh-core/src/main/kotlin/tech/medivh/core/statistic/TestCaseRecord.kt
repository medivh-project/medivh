package tech.medivh.core.statistic


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class TestCaseRecord(val name: String) {

    val threads: MutableSet<ThreadRecord> = mutableSetOf()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestCaseRecord

        if (name != other.name) return false
        if (threads != other.threads) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + threads.hashCode()
        return result
    }


}
