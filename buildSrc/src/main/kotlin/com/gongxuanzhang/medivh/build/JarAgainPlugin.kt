package com.gongxuanzhang.medivh.build

import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JarAgainPlugin : Plugin<Project> {

    override fun apply(project: Project) {

//        project.afterEvaluate {
//            val core = project.rootProject.project(":medivh-core")
//            core.tasks.named("jar") {
//                it.doLast {
//                    val agentFrom = core.buildDir.resolve("libs").resolve(agentJarName(project))
//                    val resourceDir = File(core.projectDir, "src/main/resources")
//                    val target = File(resourceDir, "medivh-agent-${project.version}.jar")
//                    agentFrom.copyTo(target, true)
//                }
//            }
//
//            core.tasks.register("jarAgain") {
//                it.mustRunAfter(":medivh-core:jar")
//                val agentFrom = core.buildDir.resolve("libs").resolve(agentJarName(project))
//                val plugin = project.rootProject.project(":medivh-plugins:medivh-plugin-gradle-kotlin")
//                val resourceDir = File(plugin.projectDir, "src/main/resources")
//                val target = File(resourceDir, "medivh-agent-${project.version}.jar")
//                agentFrom.copyTo(target, true)
//            }
//            project.tasks.filter { it.group == "publishing" || it.name == "build" }.forEach { task ->
//                task.doFirst {
//                    
//                }
//            }
//        }


    }

    private fun agentJarName(project: Project): String {
        return "medivh-core-${project.version}.jar"
    }
}
