package tech.medivh.core.jfr

import java.io.File


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ReverseFileReader(dir: File, jfrThread: JfrThread) {

    private val dataFile = dir.resolve("${jfrThread.javaThreadId}.rjfr")

    private val indexFile = dir.resolve("${jfrThread.javaThreadId}.ijfr")


    fun readOrderNode(action: (EventNode) -> Unit) {
        TODO()
    }
}
