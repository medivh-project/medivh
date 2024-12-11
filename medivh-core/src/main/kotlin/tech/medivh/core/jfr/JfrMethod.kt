package tech.medivh.core.jfr

import jdk.jfr.consumer.RecordedMethod


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JfrMethod(method: RecordedMethod) {
    val name: String = method.name
    val className = method.type.name.substringBeforeLast("/")
    val classFullName = method.type.name.replace("/", ".")

    override fun toString(): String {
        return "JfrMethod(name='$name', className='$className', classFullName='$classFullName')"
    }
}
