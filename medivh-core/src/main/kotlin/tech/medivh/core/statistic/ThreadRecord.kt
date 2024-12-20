package tech.medivh.core.statistic


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ThreadRecord(val name: String, val aggregation: ThreadAggregation) {
    var functionRoot: FunctionRecord? = null

}
