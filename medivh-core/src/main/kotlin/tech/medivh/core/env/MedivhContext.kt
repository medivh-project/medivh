package tech.medivh.core.env

import com.alibaba.fastjson2.JSONObject
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.matcher.ElementMatcher
import net.bytebuddy.matcher.ElementMatchers
import java.io.File


/**
 *
 *
 *
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class MedivhContext(vars: String?) {

    private val medivhProperties: MedivhProperties = JSONObject.parseObject(vars ?: "{}", MedivhProperties::class.java)

    init {
        check(medivhProperties.legal())
    }

    fun mode() = medivhProperties.runningMode

    fun language() = medivhProperties.language

    fun includeMatchers(): ElementMatcher<TypeDescription> {
        var matcher = ElementMatchers.none<TypeDescription>()
        val include = medivhProperties.includePackage
        if (include.isEmpty()) {
            return matcher
        }
        include.forEach {
            matcher = matcher.or(ElementMatchers.nameStartsWith(it))
        }
        return matcher
    }

    fun reportDir(): File {
        val reportDir = medivhProperties.reportDir
        check(reportDir.isNotEmpty()) { "reportDir is empty" }
        return File(reportDir)
    }

}
