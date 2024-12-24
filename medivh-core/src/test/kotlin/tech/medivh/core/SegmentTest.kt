package tech.medivh.core

import org.junit.jupiter.api.Test
import tech.medivh.core.jfr.ExternalClassifier
import tech.medivh.core.jfr.MemoryClassifier
import java.io.File
import kotlin.test.assertEquals


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class SegmentTest {

    @Test
    fun externalSortTest() {
        val file = File(this.javaClass.classLoader.getResource("medivh.jfr").file)
        val externalClassify =  ExternalClassifier().classify(file)
        val memoryClassify =  MemoryClassifier().classify(file)
        assertEquals(externalClassify,memoryClassify)
    }
}
