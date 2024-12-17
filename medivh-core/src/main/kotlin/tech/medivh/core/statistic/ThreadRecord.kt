package tech.medivh.core.statistic

import tech.medivh.core.jfr.EventNode


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ThreadRecord(val name: String) {
    var functionRoot: EventNode? = null
}
