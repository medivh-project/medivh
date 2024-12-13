package tech.medivh.core.jfr

import jdk.jfr.consumer.RecordedMethod


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JfrMethod(method: RecordedMethod) {
    val name: String = method.name
    val className = method.type.name.substringAfterLast(".")
    val classFullName = method.type.name

    fun key(): String {
        return "$classFullName#$name"
    }

    override fun toString(): String {
        return "JfrMethod(name='$name', className='$className', classFullName='$classFullName')"
    }


    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + classFullName.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as JfrMethod

        if (name != other.name) return false
        if (classFullName != other.classFullName) return false

        return true
    }
}
