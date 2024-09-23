package tech.medivh.build

import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JarAgainPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.afterEvaluate {
            val core = project.rootProject.project(":medivh-core")
            val jar = core.tasks.named("jar").get()

            val agentFrom = core.layout.buildDirectory.dir("libs").get().file(agentJarName(project)).asFile
            
            core.tasks.register("doJar", Exec::class.java) {
                if (!agentFrom.exists()) {
                    it.workingDir(project.rootDir)

                    val osName = System.getProperty("os.name").lowercase()
                    val gradlew = if (osName.contains("windows")) {
                        "gradlew.bat"
                    } else {
                        "./gradlew"
                    }
                    it.commandLine(gradlew, ":medivh-core:jar")
                }
               
                it.doLast {
                    if (agentFrom.exists()) {
                        val resourceDir = File(core.projectDir, "src/main/resources")
                        val target = File(resourceDir, "medivh-agent-${project.version}.jar")
                        agentFrom.copyTo(target, true)
                    }
                }
            }

            core.tasks.named("jar") {
                if(!agentFrom.exists()) {
                    it.dependsOn("doJar")
                }
            }
        }

    }

    private fun agentJarName(project: Project): String {
        return "medivh-core-${project.version}.jar"
    }
}
