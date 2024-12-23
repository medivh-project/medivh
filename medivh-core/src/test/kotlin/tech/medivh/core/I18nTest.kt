package tech.medivh.core

import org.junit.jupiter.api.Test


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class I18nTest {

    @Test
    fun i18nTest() {
        assert(i18n(Language.ZH, "warn.includeSkip").startsWith("Medivh 警告:没有包含任何包,请使用 medivh.include")) 
        assert(i18n(Language.EN, "warn.includeSkip").startsWith("Medivh Warn:no package to include,please use medivh.include(\"your package name\")")) 
    }
}
