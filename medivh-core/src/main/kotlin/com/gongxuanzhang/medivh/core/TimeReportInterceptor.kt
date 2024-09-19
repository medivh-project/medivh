package com.gongxuanzhang.medivh.core

import java.lang.reflect.Method
import net.bytebuddy.asm.Advice

/**
 * @author gongxuanzhangmelt@gmail.com
 * 
 */
object TimeReportInterceptor {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(@Advice.Origin method: Method) {
        println("Entering method: ${method.name}")
    }
}
