package tech.medivh.core.reporter


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
data class ExecuteInfo(
    val cost: Long,
    val methodName: String,
    val invokeClassName: String,
    val threadName: String? = null
)
