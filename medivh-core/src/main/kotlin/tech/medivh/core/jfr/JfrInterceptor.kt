package tech.medivh.core.jfr

import net.bytebuddy.asm.Advice

/**
 * @author gongxuanzhangmelt@gmail.com
 *
 */
object JfrInterceptor {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(): MethodInvokeEvent {
        return MethodInvokeEvent().apply { begin() }
    }

    @JvmStatic
    @Advice.OnMethodExit
    fun onExit(@Advice.Enter event: MethodInvokeEvent) {
        event.commit()
    }


}
