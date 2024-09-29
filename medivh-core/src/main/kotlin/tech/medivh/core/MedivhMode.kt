package tech.medivh.core

import tech.medivh.core.reporter.MultiThreadTimeReporter
import tech.medivh.core.reporter.NormalTimeReporter
import tech.medivh.core.reporter.TimeReporter


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
enum class MedivhMode(val timeReport: TimeReporter) {
    
    NORMAL(NormalTimeReporter),
    MULTI_THREAD(MultiThreadTimeReporter),
}
