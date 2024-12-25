package tech.medivh.core

import org.junit.jupiter.api.Test
import tech.medivh.core.jfr.ExternalClassifier
import tech.medivh.core.jfr.ExternalClassifier.Companion.SORT_TEMP_DIR
import tech.medivh.core.jfr.MemoryClassifier
import java.io.File
import java.nio.ByteBuffer
import kotlin.test.assertEquals


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class SegmentTest {

    @Test
    fun externalSortTest() {
        val file = File(this.javaClass.classLoader.getResource("medivh.jfr")?.file ?: return)
        val sortTemp = file.parentFile.resolve(SORT_TEMP_DIR)
        sortTemp.deleteRecursively()
        val externalClassify =  ExternalClassifier().classify(file)
        val memoryClassify =  MemoryClassifier().classify(file)
        assertEquals(externalClassify,memoryClassify)
    }
    
    @Test
    fun serializeTest() {
        val buffer = ByteBuffer.allocate(Long.SIZE_BYTES*2+Short.SIZE_BYTES).apply {
            putLong(10)
            putLong(10)
            putShort(8)
        }
        buffer.flip()
        println(buffer.getLong())
        println(buffer.getLong())
        println(buffer.getShort().toInt() and 0xFFFF)
    }
}
