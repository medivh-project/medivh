package tech.medivh.core

import java.lang.reflect.Method
import net.bytebuddy.asm.Advice
import tech.medivh.core.reporter.NormalTimeReporter

/**
 * @author gongxuanzhangmelt@gmail.com
 *
 */
object NormalInterceptor {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(@Advice.Origin method: Method) {
        NormalTimeReporter.start(method.name)
    }

    @JvmStatic
    @Advice.OnMethodExit
    fun onExit(@Advice.Origin method: Method) {
        NormalTimeReporter.end(method.name)
    }
}
