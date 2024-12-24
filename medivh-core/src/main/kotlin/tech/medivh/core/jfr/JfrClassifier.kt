package tech.medivh.core.jfr

import tech.medivh.core.statistic.TestCaseRecord
import java.io.File


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
interface JfrClassifier {

    /**
     * classify jfr file to test case record
     */
    fun classify(jfrFile: File): List<TestCaseRecord>
}
