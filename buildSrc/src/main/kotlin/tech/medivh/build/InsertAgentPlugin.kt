package tech.medivh.build

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class InsertAgentPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.afterEvaluate {

            project.tasks.named("jar") {
                it.doLast {
                    val buildJar = project.buildJar
                    val agent = extractAgentJar(buildJar)
                    if (agent == null) {
                        insertAgentJar(buildJar, project)
                    }
                }
            }
        }
        registerCheckBuild(project)
    }


    private val Project.buildJar: File
        get() {
            val name = "${project.name}-${project.version}.jar"
            return layout.buildDirectory.dir("libs").get().file(name).asFile
        }


    private fun registerCheckBuild(project: Project) {
        project.tasks.register("checkBuild") {
            it.doLast {
                val targetJar = project.buildJar
                val jar = extractAgentJar(targetJar)
                check(jar != null) {
                    val message = "Can't find agent jar in $targetJar"
                    project.logger.error(message)
                    message
                }
                check(JarFile(targetJar).getEntry("medivh.version") != null) {
                    val message = "Can't find medivh.version in $targetJar"
                    project.logger.error(message)
                    message
                }
                check(extractAgentJar(jar) == null) {
                    val message = "jar file don't have agent jar [$jar]"
                    project.logger.error(message)
                    message
                }
            }
        }

        project.tasks.named("build") {
            it.finalizedBy("checkBuild")
        }
    }

    private fun extractAgentJar(jar: File): File? {
        val outputDir = Files.createTempDirectory(UUID.randomUUID().toString()).toFile()
        val jarFile = JarFile(jar)
        val entries = jarFile.entries()
        outputDir.mkdirs()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            if (entry.name.startsWith("medivh-agent") && entry.name.endsWith(".jar")) {
                val outputFile = File(outputDir, entry.name)
                jarFile.getInputStream(entry).use { input ->
                    FileOutputStream(outputFile).use { output ->
                        input.copyTo(output)
                        return outputFile
                    }
                }
            }
        }
        return null
    }

    private fun insertAgentJar(buildJar: File, project: Project) {
        val agent = buildJar.copyTo(buildJar.parentFile.resolve("medivh-agent-${project.version}.jar"), true)
        val versionFile = File(agent.parentFile, "medivh.version")
        versionFile.writeText(project.version.toString())
        val tempFile = File("${buildJar.name}.tmp")


        JarFile(buildJar).use { jarFile ->
            JarOutputStream(FileOutputStream(tempFile)).use { jarOutput ->
                jarFile.entries().asSequence().forEach { entry ->
                    jarOutput.putNextEntry(JarEntry(entry.name))
                    jarFile.getInputStream(entry).copyTo(jarOutput)
                    jarOutput.closeEntry()
                }

                val agentEntry = JarEntry(agent.name)
                jarOutput.putNextEntry(agentEntry)
                FileInputStream(agent).use { input ->
                    input.copyTo(jarOutput)
                }

                val versionFileEntry = JarEntry(versionFile.name)
                jarOutput.putNextEntry(versionFileEntry)
                FileInputStream(versionFile).use { input ->
                    input.copyTo(jarOutput)
                }
                jarOutput.closeEntry()
            }
        }
        project.logger.debug("delete agent jar ${agent.delete()}")
        project.logger.debug("delete agent jar ${versionFile.delete()}")
        tempFile.renameTo(buildJar)
    }

}


