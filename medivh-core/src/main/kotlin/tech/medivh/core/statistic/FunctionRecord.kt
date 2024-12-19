package tech.medivh.core.statistic

import java.time.Duration


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class FunctionRecord(val name: String, val className: String, var duration: Duration, var count: Int = 1) {
    val children = mutableListOf<FunctionRecord>()
}
