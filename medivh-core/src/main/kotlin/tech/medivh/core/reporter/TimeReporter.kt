package tech.medivh.core.reporter

import tech.medivh.core.MethodSetup


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
interface TimeReporter {


    fun setup(methodSetup: MethodSetup)


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
