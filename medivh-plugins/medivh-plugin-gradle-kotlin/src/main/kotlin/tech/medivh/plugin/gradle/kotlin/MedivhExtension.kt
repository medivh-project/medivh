package tech.medivh.plugin.gradle.kotlin

import java.io.File
import java.time.LocalDate
import java.util.*
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.SetProperty
import tech.medivh.core.MedivhParam
import javax.inject.Inject


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class MedivhExtension @Inject constructor(objects: ObjectFactory, private val project: Project) {

    val includePackage: SetProperty<String> = objects.setProperty(String::class.java)

    fun include(packageName: String) {
        val currentPackages = includePackage.getOrElse(emptySet())
        includePackage.set(currentPackages + packageName)
    }

    fun skip(): Boolean {
        return includePackage.getOrElse(emptySet()).isEmpty()
    }

    fun toParams(): String {
        val sb = StringBuilder()
        val packageNames = includePackage.getOrElse(emptySet())
        if (packageNames.isNotEmpty()) {
            sb.append("${MedivhParam.INCLUDE.key}=${packageNames.joinToString(",")}").append(";")
        }
        sb.append("${MedivhParam.REPORT_DIR.key}=${reportDir()}")
        return sb.toString()
    }

    private fun reportDir(): String {
        val testToken = UUID.randomUUID().toString().replace("-", "")
        LocalDate.now().apply {
            val reportRoot = project.layout.buildDirectory.dir("medivh/reports").get().asFile
            val targetDir = File(reportRoot, "/$year$monthValue$dayOfMonth/$testToken")
            targetDir.mkdirs()
            return targetDir.path
        }
    }
}
