package tech.medivh.core.reporter


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
interface TimeReporter {

    /**
     * method start
     */
    fun start(token: String)

    /**
     * method end
     */
    fun end(token: String)

    /**
     * generate html
     */
    fun htmlTemplateName(): String

    /**
     * generate json string
     */
    fun generateJsonString(): String
}
