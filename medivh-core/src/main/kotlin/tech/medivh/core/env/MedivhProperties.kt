package tech.medivh.core.env

import tech.medivh.core.Language
import java.util.*


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class MedivhProperties {

    var includePackage = hashSetOf<String>()

    var runningMode: RunningMode = RunningMode.NORMAL

    var language: Language = Language.valueOf(Locale.getDefault().language.uppercase())

    var reportDir: String = ""

    var ignoreBelowCount = 0


    fun legal(): Boolean {
        return reportDir.isNotEmpty()
    }

}
