package tech.medivh.junit.extension

import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import tech.medivh.core.jfr.JfrInterceptor

/**
 * Used to isolate different test cases
 * @author gongxuanzhangmelt@gmail.com
 */
class MedivhJunitExtension : BeforeEachCallback {

    override fun beforeEach(context: ExtensionContext) {
        JfrInterceptor.updateToken(context.displayName)
    }
}
