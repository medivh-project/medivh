package tech.medivh.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


/**
 *
 * insert some file into jar
 *
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class InsertAgentPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.afterEvaluate {

            project.tasks.named("jar") {
                it.doLast {
                    val buildJar = project.buildJar
                    if (!buildJar.hasAgentJar) {
                        File(buildJar.name).apply {
                            val agent = copyTo(parentFile.resolve("medivh-agent-${project.version}.jar"), true)
                            val versionFile = File(agent.parentFile, "medivh.version")
                            versionFile.writeText(project.version.toString())
                            insertFileIntoJar(buildJar, agent, versionFile, zipReport(project))
                        }
                    }
                }
            }
        }
        registerCheckBuild(project)
    }


    private fun registerCheckBuild(project: Project) {
        project.tasks.register("checkBuild") {
            it.doLast {
                val targetJar = project.buildJar
                val agentJar = targetJar.getJarEntry("medivh-agent-${project.version}.jar")
                check(agentJar != null) {
                    val message = "Can't find medivh-agent-${project.version}.jar in $targetJar"
                    project.logger.error(message)
                    message
                }
                check(targetJar.getEntry("medivh.version") != null) {
                    val message = "Can't find medivh.version in $targetJar"
                    project.logger.error(message)
                    message
                }
            }
        }

        project.tasks.named("build") {
            it.finalizedBy("checkBuild")
        }
    }

    private fun zipReport(project: Project): File {
        val reportDir = project.resources.text.fromFile("src/main/resources/report/").asFile()
        val zipReportTargetFile = project.layout.buildDirectory.dir("libs").get().file("medivh-report.zip").asFile
        ZipOutputStream(FileOutputStream(zipReportTargetFile)).use { zipOutput ->
            reportDir.walkTopDown().filter { it.isFile }.forEach { file ->
                val zipEntry = ZipEntry(file.relativeTo(reportDir).path)
                zipOutput.putNextEntry(zipEntry)
                FileInputStream(file).use { input ->
                    input.copyTo(zipOutput)
                }
                zipOutput.closeEntry()
            }
        }
        return zipReportTargetFile
    }

    private fun insertFileIntoJar(into: JarFile, vararg fileToInsert: File) {
        val jarFile = File(into.name)
        val tempJarFile = File(jarFile.parent, "${jarFile.name}.tmp")

        into.use { jar ->
            JarOutputStream(FileOutputStream(tempJarFile)).use { jos ->
                jar.entries().asSequence().forEach { entry ->
                    if (!entry.isDirectory) {
                        jar.getInputStream(entry).use { inputStream ->
                            jos.putNextEntry(JarEntry(entry.name))
                            inputStream.copyTo(jos)
                            jos.closeEntry()
                        }
                    }
                }

                fileToInsert.forEach { file ->
                    FileInputStream(file).use { inputStream ->
                        jos.putNextEntry(JarEntry(file.name))
                        inputStream.copyTo(jos)
                        jos.closeEntry()
                    }
                }
            }
        }

        if (!jarFile.delete()) {
            throw IOException("Failed to delete original jar file: ${jarFile.absolutePath}")
        }
        if (!tempJarFile.renameTo(jarFile)) {
            throw IOException("Failed to rename temp jar file to: ${jarFile.absolutePath}")
        }
    }


    private val Project.buildJar: JarFile
        get() {
            val name = "${project.name}-${project.version}.jar"
            return JarFile(layout.buildDirectory.dir("libs").get().file(name).asFile)
        }

    private val JarFile.hasAgentJar: Boolean
        get() {
            val entries = entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                if (entry.name.startsWith("medivh-agent") && entry.name.endsWith(".jar")) {
                    return true
                }
            }
            return false
        }
}


