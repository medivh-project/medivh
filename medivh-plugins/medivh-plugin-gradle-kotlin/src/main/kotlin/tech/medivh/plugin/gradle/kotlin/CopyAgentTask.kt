package tech.medivh.plugin.gradle.kotlin

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class CopyAgentTask @Inject constructor(@Input val version: String) : MedivhTask() {

    init {
        this.description = "copy medivh agent to build medivh directory"
    }


    @get:OutputFile
    val outputFile = project.layout.buildDirectory.dir("medivh").get().file(agentName()).asFile

    @TaskAction
    fun copyAgent() {
        this.javaClass.classLoader.getResourceAsStream(agentName())?.let { input ->
            outputFile.outputStream().use { it.write(input.readBytes()) }
        } ?: throw IllegalStateException("agent [${agentName()}] not found")
    }

    private fun agentName(): String {
        return "medivh-agent-${version}.jar"
    }
}
