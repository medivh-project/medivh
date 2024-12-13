package tech.medivh.plugin.gradle.kotlin

import org.junit.jupiter.api.Test
import tech.medivh.core.jfr.JfrReverser
import java.io.File


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class AnalysisTest {


    @Test
    fun reverseTest() {
        println(1)
        JfrReverser(File("/Users/gongxuanzhang/Desktop/medivh.jfr")).writeReverse()

    }

}
