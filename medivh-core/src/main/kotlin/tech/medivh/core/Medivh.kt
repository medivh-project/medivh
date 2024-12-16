package tech.medivh.core

import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.asm.Advice
import net.bytebuddy.description.annotation.AnnotationDescription
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.matcher.ElementMatcher
import net.bytebuddy.matcher.ElementMatchers
import tech.medivh.api.DebugTime
import tech.medivh.core.env.MedivhContext
import tech.medivh.core.env.RunningMode
import tech.medivh.core.jfr.JfrInterceptor
import tech.medivh.core.reporter.TagMethod
import tech.medivh.core.reporter.TagMethodLogFileWriter
import java.lang.instrument.Instrumentation
import java.util.*


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object Medivh {

    @JvmStatic
    fun premain(agentArgs: String?, inst: Instrumentation) {
        println("Medivh 开始")
        val properties = String(Base64.getUrlDecoder().decode(agentArgs))
        val context = MedivhContext(properties)
        context.reportDir().mkdirs()
        val writer = TagMethodLogFileWriter(context.reportDir().resolve(TagMethod.FILE_NAME))
        Runtime.getRuntime().addShutdownHook(Thread {
            writer.close()
        })

        val advice = Advice.to(JfrInterceptor::class.java)

        val debugTimeName = DebugTime::class.java.name
        AgentBuilder.Default().type(context.includeMatchers())
            .transform { builder, desc, _, _, _ ->
                desc.declaredMethods.forEach { method ->
                    val debugTime = method.asDefined().declaredAnnotations.find {
                        it.annotationType.name == debugTimeName
                    }
                    debugTime?.let {
                        val debugTimeDesc = resolveDebugTime(it)
                        writer.writeMethod(TagMethod(method.name, desc.name, debugTimeDesc.expectTime))
                    }
                }
                val methodIntercept : ElementMatcher<MethodDescription> = if (context.mode() == RunningMode.NORMAL) {
                    ElementMatchers.isAnnotatedWith(DebugTime::class.java)
                } else {
                    //  exclude lambda method
                    ElementMatchers.not(ElementMatchers.isSynthetic())
                        .and(ElementMatchers.not(ElementMatchers.nameContains("lambda$")))
                }
                builder.method(methodIntercept)
                    .intercept(advice)

            }.installOn(inst)

    }


    private fun resolveDebugTime(debugTime: AnnotationDescription): DebugTimeDesc {
        return DebugTimeDesc(debugTime.getValue("expectTime").resolve() as Long)
    }

}

