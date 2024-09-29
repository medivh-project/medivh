package tech.medivh.core

import java.lang.reflect.Method
import net.bytebuddy.asm.Advice
import tech.medivh.core.reporter.MultiThreadTimeReporter

/**
 * @author gongxuanzhangmelt@gmail.com
 *
 */
object MultiThreadInterceptor {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(@Advice.Origin method: Method) {
        MultiThreadTimeReporter.start(method.name)
    }

    @JvmStatic
    @Advice.OnMethodExit
    fun onExit(@Advice.Origin method: Method) {
        MultiThreadTimeReporter.end(method.name)
    }
}
