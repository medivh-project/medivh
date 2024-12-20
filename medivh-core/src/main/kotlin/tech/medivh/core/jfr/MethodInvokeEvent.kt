package tech.medivh.core.jfr

import jdk.jfr.Event
import jdk.jfr.Label


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class MethodInvokeEvent : Event() {
    @Label("testCase")
    var testCase: String? = null


}
