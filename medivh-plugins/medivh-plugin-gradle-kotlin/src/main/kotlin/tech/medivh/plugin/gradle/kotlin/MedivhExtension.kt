package tech.medivh.plugin.gradle.kotlin

import java.io.File
import java.time.LocalDate
import java.util.*
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.SetProperty
import tech.medivh.core.MedivhMode
import tech.medivh.core.MedivhParam
import javax.inject.Inject


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class MedivhExtension @Inject constructor(objects: ObjectFactory, private val project: Project) {

    private val includePackage: SetProperty<String> = objects.setProperty(String::class.java)

    private var mode: MedivhMode = MedivhMode.NORMAL

    fun include(packageName: String) {
        val currentPackages = includePackage.getOrElse(emptySet())
        includePackage.set(currentPackages + packageName)
    }

    fun mutliThread() {
        mode = MedivhMode.MULTI_THREAD
    }

    internal fun skip(): Boolean {
        return includePackage.getOrElse(emptySet()).isEmpty()
    }

    internal fun toParams(): String {
        val params = hashMapOf<String, String>()
        val packageNames = includePackage.getOrElse(emptySet())
        params[MedivhParam.INCLUDE.key] = packageNames.joinToString(",")
        params[MedivhParam.REPORT_DIR.key] = reportDir()
        params[MedivhParam.MODE.key] = mode.name
        
        return params.map { "${it.key}=${it.value}" }.joinToString(";")
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

