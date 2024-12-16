package tech.medivh.plugin.gradle.kotlin

import tech.medivh.core.jfr.EventNode
import tech.medivh.core.jfr.JfrThread


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class TestCase(private val name: String) {

    private val threadTreeMap = mutableMapOf<JfrThread, EventNode>()



}
