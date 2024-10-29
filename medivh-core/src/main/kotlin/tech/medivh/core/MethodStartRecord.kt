package tech.medivh.core


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
data class MethodStartRecord(
    val invokeClassName: String,
    val methodName: String,
    val startTime: Long = System.currentTimeMillis()
)
