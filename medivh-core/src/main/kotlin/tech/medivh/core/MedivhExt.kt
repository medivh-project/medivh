package tech.medivh.core

import java.text.MessageFormat


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/

const val githubUrl = "https://www.github.com/medivh-project/meidvh"

fun unexpectedly(): Nothing {
    throw IllegalStateException("There seems to be a problem, please report back on $githubUrl")
}

fun i18n(language: Language, key: String, vararg arguments: String): String {
    return MessageFormat.format(language.bundle.getString(key), *arguments)
}

fun String.classAndMethod(): Pair<String, String> {
    val className = substringBefore("#")
    val methodName = substring(className.length + 1)
    return className to methodName
}
