package tech.medivh.core


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
enum class MedivhParam(val type: Class<*>, val key: String, val default: Any?, val resolve: ParamResolver<*>) {

    INCLUDE(List::class.java, "include", null, ListResolver),
    REPORT_DIR(String::class.java, "reportDir", null, StringResolver),
    MODE(String::class.java, "mode", null, StringResolver);

    companion object {
        private val keyMap = entries.associateBy { it.key }
        fun valueOfKey(key: String): MedivhParam? {
            return keyMap[key]
        }
    }

}
