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


    fun key(): Long {
        return javaThreadId
    }

    override fun toString(): String {
        return javaName
    }


    override fun hashCode(): Int {
        return javaThreadId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as JfrThread

        if (javaThreadId != other.javaThreadId) return false

        return true
    }

}
