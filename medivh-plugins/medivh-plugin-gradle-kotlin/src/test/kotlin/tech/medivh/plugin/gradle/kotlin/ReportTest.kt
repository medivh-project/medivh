package tech.medivh.plugin.gradle.kotlin

import org.junit.jupiter.api.Test
import tech.medivh.core.jfr.JfrReverser
import tech.medivh.core.jfr.JfrThread
import tech.medivh.core.jfr.ThreadEventTreeBuilder
import java.io.File


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ReportTest {

    @Test
    fun testReport() {
        val jfrFile = File(this.javaClass.classLoader.getResource("medivh.jfr").toURI())
        val reverser = JfrReverser(jfrFile)
        reverser.writeReverse()
        val threadTree = mutableMapOf<JfrThread, ThreadEventTreeBuilder>()
        reverser.readReverse { thread, node ->
            threadTree.computeIfAbsent(thread) {
                ThreadEventTreeBuilder(it.javaName)
            }.processNode(node)
        }
        println(1)
    }
}
