package tech.medivh.core.reporter


/**
 *
 * method description that add DebugTime annotation
 *
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
data class TagMethod(val methodName: String, val className: String, val expectTime: Long) {

    companion object {
        const val FILE_NAME = "tagMethod.log"
    }
}
