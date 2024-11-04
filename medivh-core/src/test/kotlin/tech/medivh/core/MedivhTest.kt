package tech.medivh.core

import net.bytebuddy.ByteBuddy
import net.bytebuddy.asm.Advice
import net.bytebuddy.matcher.ElementMatchers
import org.junit.jupiter.api.Test
import tech.medivh.api.DebugTime
import tech.medivh.core.interceptor.NormalInterceptor


class MedivhTest {


    @Test
    fun medivhTest() {
        val advice = Advice.to(NormalInterceptor::class.java)
        val testInstance: TestClass = ByteBuddy()
            .subclass(TestClass::class.java)
            .method(ElementMatchers.isAnnotatedWith(DebugTime::class.java))
            .intercept(advice)
            .make()
            .load(javaClass.classLoader)
            .loaded
            .getDeclaredConstructor()
            .newInstance()
        testInstance.testMethod()
    }

    @Test
    fun methodTest() {
        val advice = Advice.to(NormalInterceptor::class.java)
        val desc = ByteBuddy()
            .subclass(TestClass::class.java)
            .method(ElementMatchers.any())
            .intercept(advice)
            .transform { instrumentedType, target ->
                println(instrumentedType)
                println(target)
                target
            }
            .make()
    }
}