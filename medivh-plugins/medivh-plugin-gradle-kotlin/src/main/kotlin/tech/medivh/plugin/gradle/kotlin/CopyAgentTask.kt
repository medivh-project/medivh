package tech.medivh.plugin.gradle.kotlin

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class CopyAgentTask : MedivhTask() {

    init {
        this.description = "copy medivh agent to build medivh directory"
    }

    @get:Input
    var version: String = ""

    @get:OutputFile
    val outputFile = project.buildDir.resolve("medivh").resolve(agentName())

    @TaskAction
    fun copyAgent() {
        this.javaClass.classLoader.getResourceAsStream(agentName())?.let { input ->
            outputFile.outputStream().use { it.write(input.readBytes()) }
        } ?: throw IllegalStateException("agent not found")
    }

    private fun agentName(): String {
        return "medivh-agent-${version}.jar"
    }
}
