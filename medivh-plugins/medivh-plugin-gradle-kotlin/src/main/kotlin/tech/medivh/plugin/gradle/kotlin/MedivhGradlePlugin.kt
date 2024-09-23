package tech.medivh.plugin.gradle.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class MedivhGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {

        //   todo  build src auto 
        val byteBuddyVersion = "1.15.1"

        val medivhExtension = project.extensions.create("medivh", MedivhExtension::class.java)

        project.dependencies.add("implementation", "tech.medivh:medivh-api:0.0.1")
        project.dependencies.add("testImplementation", "net.bytebuddy:byte-buddy-agent:$byteBuddyVersion")
        project.dependencies.add("testImplementation", "net.bytebuddy:byte-buddy:$byteBuddyVersion")
        val copyAgent = project.tasks.register("copyAgent", CopyAgentTask::class.java){
            it.version = project.version.toString()
        }
        project.afterEvaluate {
            it.tasks.withType(Test::class.java) { test ->
                test.dependsOn("copyAgent")
                test.jvmArgs("-javaagent:${copyAgent.get().outputFile}=${medivhExtension.toParams()}")
            }
        }
    }
}
