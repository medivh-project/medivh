package tech.medivh.core.jfr

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.io.File


class ReverseFileReaderTest {

    @Test
    fun readTest() {
        val thread = Mockito.mock(JfrThread::class.java)
        `when`(thread.javaThreadId).thenReturn(23)
        val reader = ReverseFileReader(
            File("/Users/gongxuanzhang/dev/github/gradle-demo/build/medivh/reports/20241217/d3da4e2337154f5db83dc12f77e84ae8"),
            thread
        )
        var count = 0
        println("start read")
        reader.readOrderNode {
            count++
        }
        println(count)
    }

}
