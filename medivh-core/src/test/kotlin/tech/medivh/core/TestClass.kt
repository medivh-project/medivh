package tech.medivh.core

import com.alibaba.fastjson2.JSONArray
import org.junit.jupiter.api.Test
import tech.medivh.api.DebugTime
import java.io.File


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class TestClass {


    @DebugTime
    fun testMethod() {
        println(1)
    }

    @Test
    fun testAnalysis() {
        val parse = JSONArray.parse(File(this.javaClass.classLoader.getResource("aa.json").file).readText())
        println(1)
    }
}
