package tech.medivh.core

import java.io.File
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.matcher.ElementMatcher
import net.bytebuddy.matcher.ElementMatchers


/**
 *
 *
 * a=b;c=d;list=1,2,3
 *
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
@Suppress("UNCHECKED_CAST")
class MedivhContext(vars: String?) {


    private val include = mutableListOf<String>()

    private var reportDir: String? = null

    private lateinit var mode: MedivhMode

    private lateinit var language: Language

    init {
        vars?.let {
            it.split(";").forEach { kv ->
                val split = kv.split("=")
                check(split.size == 2) { "invalid args $vars" }
                MedivhParam.valueOfKey(split[0])?.let { param ->
                    val value = param.resolve.resolve(split[1])
                    when (param) {
                        MedivhParam.INCLUDE -> include.addAll(value as List<String>)
                        MedivhParam.REPORT_DIR -> reportDir = value as String
                        MedivhParam.MODE -> mode = MedivhMode.valueOf(value as String)
                        MedivhParam.LANGUAGE -> language = value as Language
                    }
                }
            }
        }
        
    }

    fun mode() = mode

    fun language() = language

    fun includeMatchers(): ElementMatcher<TypeDescription> {
        var matcher = ElementMatchers.none<TypeDescription>()
        if (include.isEmpty()) {
            return matcher
        }
        include.forEach {
            matcher = matcher.or(ElementMatchers.nameStartsWith(it))
        }
        return matcher
    }

    fun targetDir() = reportDir?.let { File(it) } ?: throw IllegalArgumentException("reportDir is null")

}
