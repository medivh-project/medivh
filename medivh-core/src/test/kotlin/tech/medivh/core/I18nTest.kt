package tech.medivh.core

import org.junit.jupiter.api.Test


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class I18nTest {

    @Test
    fun i18nTest() {
        println(i18n(Language.ZH, "warn.includeSkip"))
        println(i18n(Language.EN, "warn.includeSkip"))
    }
}