package tech.medivh.core

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

    init {
        vars?.let {
            it.split(";").forEach { kv ->
                val split = kv.split("=")
                check(split.size == 2) { "invalid args $vars" }
                MedivhParam.valueOfKey(split[0])?.let { param ->
                    val value = param.resolve.resolve(split[1])
                    when (param) {
                        MedivhParam.INCLUDE -> include.addAll(value as List<String>)
                    }
                }
            }
        }
    }


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

}
