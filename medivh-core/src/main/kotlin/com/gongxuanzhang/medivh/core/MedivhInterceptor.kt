package com.gongxuanzhang.medivh.core

import java.lang.reflect.Method
import net.bytebuddy.asm.Advice

/**
 * @author gongxuanzhangmelt@gmail.com
 * 
 */
object MedivhInterceptor {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(@Advice.Origin method: Method) {
        TimeReporter.start(method.name)
    }
    
    @JvmStatic
    @Advice.OnMethodExit
    fun onExit(@Advice.Origin method: Method) {
        TimeReporter.end(method.name)
    }
}
