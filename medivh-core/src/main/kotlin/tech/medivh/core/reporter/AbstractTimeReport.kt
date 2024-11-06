package tech.medivh.core.reporter

import tech.medivh.core.DebugTimeDesc
import tech.medivh.core.MethodSetup


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
abstract class AbstractTimeReport : TimeReporter {

    protected val methodSetupMap = HashMap<String, DebugTimeDesc>()

    override fun setup(methodSetup: MethodSetup) {
        methodSetupMap[methodSetup.methodToken] = methodSetup.debugTimeDesc
    }


}