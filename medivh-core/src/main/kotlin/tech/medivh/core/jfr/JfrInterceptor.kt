package tech.medivh.core.jfr

import net.bytebuddy.asm.Advice

/**
 * @author gongxuanzhangmelt@gmail.com
 *
 */
object JfrInterceptor {

    @JvmStatic
    var token: String = "init"


    @JvmStatic
    fun updateToken(newToken: String) {
        token = newToken
    }

    @JvmStatic
    fun getToken1(): String {
        return token
    }

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(): MethodInvokeEvent {
        return MethodInvokeEvent().apply {
            testCase = getToken1()
            begin()
        }
    }

    @JvmStatic
    @Advice.OnMethodExit
    fun onExit(@Advice.Enter event: MethodInvokeEvent) {
        event.commit()
    }


}
