package tech.medivh.core

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import tech.medivh.core.jfr.JfrInterceptor
import kotlin.test.Ignore


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class SupportUnused {

    @Test
    @Ignore
    fun testUnused() {
        JfrInterceptor.onEnter()
        JfrInterceptor.onExit(mock())
        Medivh
    }
}
