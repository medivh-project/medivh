package tech.medivh.core.reporter

import tech.medivh.core.jfr.JfrAnalyzer
import java.io.File


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JfrReporter(private val jfrFile: File) {

    private val analyzer = JfrAnalyzer()


    fun report() {
        analyzer.analysis(jfrFile) {
            println(it)
        }
    }
}
