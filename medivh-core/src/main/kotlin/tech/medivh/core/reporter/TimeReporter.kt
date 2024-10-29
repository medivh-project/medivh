package tech.medivh.core.reporter


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
interface TimeReporter {

    fun report(executeInfo: ExecuteInfo)

    /**
     * generate html
     */
    fun htmlTemplateName(): String

    /**
     * generate json string
     */
    fun generateJsonString(): String
}
