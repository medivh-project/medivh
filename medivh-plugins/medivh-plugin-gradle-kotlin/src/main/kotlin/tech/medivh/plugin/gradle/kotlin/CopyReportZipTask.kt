package tech.medivh.plugin.gradle.kotlin

import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class CopyReportZipTask : MedivhTask() {

    init {
        this.description = "copy report zip to build medivh directory"
    }

    private val zipName = "medivh-report.zip"

    @get:OutputFile
    val outputFile = project.layout.buildDirectory.dir("medivh").get().file(zipName).asFile

    @TaskAction
    fun copyReportZip() {
        this.javaClass.classLoader.getResourceAsStream(zipName)?.let { input ->
            outputFile.outputStream().use { it.write(input.readBytes()) }
        } ?: throw IllegalStateException("[${zipName}] not found")
    }

}
