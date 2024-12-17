package tech.medivh.junit.extension

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import tech.medivh.core.jfr.JfrInterceptor

class MedivhJunitExtension : BeforeTestExecutionCallback {
    override fun beforeTestExecution(context: ExtensionContext) {
        println("开始执行测试: " + context.displayName)
        JfrInterceptor.updateToken(context.displayName)
    }
}
