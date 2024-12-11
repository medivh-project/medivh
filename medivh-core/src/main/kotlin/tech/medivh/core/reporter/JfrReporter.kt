package tech.medivh.core.reporter

import tech.medivh.core.jfr.JfrAnalyzer
import tech.medivh.core.jfr.JfrMethod
import tech.medivh.core.statistic.JfrStatistic
import java.io.File


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JfrReporter(private val jfrFile: File) {

    private val analyzer = JfrAnalyzer()

    private val statistic: MutableMap<JfrMethod, JfrStatistic> = hashMapOf()

    fun report() {
        analyzer.analysis(jfrFile) {
            statistic.merge(it.method, JfrStatistic(it), JfrStatistic::merge)
        }

        println(statistic)
    }


}
