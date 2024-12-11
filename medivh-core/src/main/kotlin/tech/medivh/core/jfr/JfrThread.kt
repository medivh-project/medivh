package tech.medivh.core.jfr

import jdk.jfr.consumer.RecordedThread


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JfrThread(thread: RecordedThread) {
    val osName = thread.osName
    val osThreadId = thread.osThreadId
    val javaName = thread.javaName
    val javaThreadId = thread.javaThreadId


    fun key(): String {
        return javaName
    }

    override fun toString(): String {
        return "JfrThread(osName='$osName', osThreadId=$osThreadId, javaName='$javaName', javaThreadId=$javaThreadId)"
    }
}
