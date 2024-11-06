package tech.medivh.plugin.gradle.kotlin

import com.alibaba.fastjson2.JSONObject
import java.io.File
import java.time.LocalDate
import java.util.*
import org.gradle.api.Project
import tech.medivh.core.Language
import tech.medivh.core.env.MedivhProperties
import tech.medivh.core.env.RunningMode
import javax.inject.Inject


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class MedivhExtension @Inject constructor(private val project: Project) {

    internal val properties = MedivhProperties()

    fun include(packageName: String) {
        properties.includePackage += packageName
    }

    fun mutliThread() {
        properties.runningMode = RunningMode.MULTI_THREAD
    }

    fun ignoreBelowCount(count: Int) {
        properties.ignoreBelowCount = count
    }

    fun language(language: Language) {
        properties.language = language
    }

    internal fun skip(): Boolean {
        return properties.includePackage.isEmpty()
    }


    internal fun javaagentArgs(): String {
        if (properties.reportDir.isEmpty()) {
            properties.reportDir = reportDir()
        }
        return JSONObject.toJSONString(properties)
    }

    private fun reportDir(): String {
        val testToken = UUID.randomUUID().toString().replace("-", "")
        LocalDate.now().apply {
            val reportRoot = project.layout.buildDirectory.dir("medivh/reports").get().asFile
            val targetDir = File(reportRoot, "/$year$monthValue$dayOfMonth/$testToken")
            return targetDir.path
        }
    }
}

