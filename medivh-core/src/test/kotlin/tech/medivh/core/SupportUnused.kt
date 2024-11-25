package tech.medivh.core

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import tech.medivh.core.interceptor.MultiThreadInterceptor
import tech.medivh.core.interceptor.NormalInterceptor
import kotlin.test.Ignore


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class SupportUnused {

    @Test
    @Ignore
    fun testUnused() {
        NormalInterceptor.onEnter()
        NormalInterceptor.onExit(mock(), 0)
        Medivh
        MultiThreadInterceptor.onEnter()
        MultiThreadInterceptor.onExit(mock(), 0)
    }
}
