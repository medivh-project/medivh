package tech.medivh.build

import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.internal.impldep.org.bouncycastle.asn1.x500.style.RFC4519Style.o
import sun.tools.jar.resources.jar


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JarAgainPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.afterEvaluate {
            val onlyJar: String? = project.findProperty("onlyJar") as String? ?: "false"
            if (!onlyJar.toBoolean()) {
                registerDojar(project)
                val core = project.rootProject.project(":medivh-core")
                val jar = core.tasks.named("jar") {
                    it.dependsOn("doJar")
                    it.doLast {
                        val agentFrom = core.layout.buildDirectory.dir("libs").get().file(agentJarName(project)).asFile
                        val resourceDir = File(core.projectDir, "src/main/resources")
                        val target = File(resourceDir, "medivh-agent-${project.version}.jar")
                        agentFrom.copyTo(target, true)
                    }
                }
            }
        }
    }

    private fun registerDojar(project: Project) {
        val core = project.rootProject.project(":medivh-core")
        core.tasks.register("doJar", Exec::class.java) {
            it.workingDir(project.rootProject.rootDir)
            val osName = System.getProperty("os.name").lowercase()
            val gradlew = if (osName.contains("windows")) {
                "gradlew.bat"
            } else {
                "./gradlew"
            }
            it.commandLine(gradlew, ":medivh-core:jar", "-PonlyJar=true")
        }
    }

    private fun agentJarName(project: Project): String {
        return "medivh-core-${project.version}.jar"
    }
}
