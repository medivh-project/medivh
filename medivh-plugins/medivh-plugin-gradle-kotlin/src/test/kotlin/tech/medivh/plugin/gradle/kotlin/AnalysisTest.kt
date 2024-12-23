package tech.medivh.plugin.gradle.kotlin

import org.gradle.internal.impldep.org.junit.Ignore
import org.junit.jupiter.api.Test
import tech.medivh.core.jfr.JfrEventClassifier
import java.io.File


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class AnalysisTest {


    @Test
    @Ignore
    fun reverseTest() {
        JfrEventClassifier(File(this.javaClass.classLoader.getResource("medivh.jfr").path)).classify()
    }


}
