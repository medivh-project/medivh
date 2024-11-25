package tech.medivh.plugin.gradle.kotlin

import org.gradle.internal.impldep.org.junit.Ignore
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import tech.medivh.core.Language


class MedivhExtensionTest {
    @Test
    @Ignore
    fun supportUnused() {
        val extension = mock<MedivhExtension>()
        extension.include("test")
        extension.multiThread()
        extension.ignoreBelowCount(1)
        extension.language(Language.ZH)
    }
}
