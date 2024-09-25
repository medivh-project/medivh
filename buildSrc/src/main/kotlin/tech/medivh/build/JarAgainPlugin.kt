package tech.medivh.build

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.UUID
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JarAgainPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.afterEvaluate {

            val core = project.medivhCore()
            core.tasks.named("jar") {
                it.doLast {
                    val buildJar = core.buildJar
                    val agent = extractAgentJar(buildJar)
                    if (agent == null) {
                        insertAgentJar(buildJar, core)
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
                val core = project.medivhCore()
                val targetJar = core.layout.buildDirectory.dir("libs").get().file("medivh-core-${core.version}.jar")
                val jar = extractAgentJar(targetJar.asFile)
                check(jar != null) {
                    val message = "Can't find agent jar in ${targetJar.asFile}"
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

    private fun Project.medivhCore(): Project {
        return rootProject.project(":medivh-core")
    }

    private fun insertAgentJar(buildJar: File, project: Project) {
        val jarFile = JarFile(buildJar)
        val agent = buildJar.copyTo(buildJar.parentFile.resolve("medivh-agent-${project.version}.jar"), true)
        JarOutputStream(FileOutputStream(buildJar, true)).use { jarOutput ->

            // 创建 JAR 条目
            val jarEntry = JarEntry(agent.name)
            jarOutput.putNextEntry(jarEntry)

            // 将文件内容写入 JAR 条目
            FileInputStream(agent).use { input ->
                input.copyTo(jarOutput)
            }
            jarOutput.closeEntry()
        }
    }

}


