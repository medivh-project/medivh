package tech.medivh.core


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
enum class MedivhParam(val type: Class<*>, val key: String, val resolve: ParamResolver<*>) {

    INCLUDE(List::class.java, "include", ListResolver),
    REPORT_DIR(String::class.java, "reportDir", StringResolver),
    MODE(String::class.java, "mode", StringResolver),
    LANGUAGE(Language::class.java, "language", LanguageResolver);

    companion object {
        private val keyMap = entries.associateBy { it.key }
        fun valueOfKey(key: String): MedivhParam? {
            return keyMap[key]
        }
    }

}
