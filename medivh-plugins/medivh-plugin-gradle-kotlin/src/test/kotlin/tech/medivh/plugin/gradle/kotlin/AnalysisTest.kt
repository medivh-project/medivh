package tech.medivh.plugin.gradle.kotlin

import org.junit.jupiter.api.Test
import tech.medivh.core.jfr.JfrEventClassifier
import java.io.File


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class AnalysisTest {


    @Test
    fun reverseTest() {
        val classify = JfrEventClassifier(File(this.javaClass.classLoader.getResource("medivh.jfr").path)).classify()
        println(1)
    }


}
