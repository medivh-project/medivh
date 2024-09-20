package com.gongxuanzhang.medivh.core

import com.gongxuanzhang.medivh.api.DebugTime
import java.lang.instrument.Instrumentation
import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.asm.Advice
import net.bytebuddy.matcher.ElementMatchers


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object Medivh {

    @JvmStatic
    fun premain(agentArgs: String?, inst: Instrumentation) {

        val context = MedivhContext(agentArgs)

        Runtime.getRuntime().addShutdownHook(Thread {
            println(TimeReporter.toHtml())
        })

        AgentBuilder.Default().type(context.includeMatchers())
            .transform { builder, _, _, _, _ ->
                builder.method(ElementMatchers.isAnnotatedWith(DebugTime::class.java))
                    .intercept(Advice.to(MedivhInterceptor::class.java))
            }.installOn(inst)
    }


}

