package tech.medivh.core

import net.bytebuddy.description.method.MethodDescription
import tech.medivh.core.reporter.ExecuteInfo


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object MethodToken {


    fun fromMethodDescription(desc: MethodDescription.InDefinedShape): String {
        return "${desc.declaringType.name}#${desc.name}"
    }

    fun fromExecuteInfo(executeInfo: ExecuteInfo): String {
        return "${executeInfo.invokeClassName}#${executeInfo.methodName}"
    }
}