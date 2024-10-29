package tech.medivh.core.interceptor

import java.lang.reflect.Method
import net.bytebuddy.asm.Advice
import tech.medivh.core.reporter.ExecuteInfo
import tech.medivh.core.reporter.MultiThreadTimeReporter

/**
 * @author gongxuanzhangmelt@gmail.com
 *
 */
object MultiThreadInterceptor {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(): Long {
        return System.currentTimeMillis()
    }

    @JvmStatic
    @Advice.OnMethodExit
    fun onExit(@Advice.Origin method: Method, @Advice.Enter startTime: Long) {
        val info = ExecuteInfo(
            System.currentTimeMillis() - startTime,
            method.name,
            method.declaringClass.name,
            Thread.currentThread().name
        )
        MultiThreadTimeReporter.report(info)
    }


}
