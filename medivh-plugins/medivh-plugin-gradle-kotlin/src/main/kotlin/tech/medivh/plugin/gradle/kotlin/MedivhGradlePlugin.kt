package tech.medivh.plugin.gradle.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class MedivhGradlePlugin : Plugin<Project> {


    override fun apply(project: Project) {

        val medivhExtension = project.extensions.create("medivh", MedivhExtension::class.java, project.objects)

        val resourceStream = this::class.java.classLoader.getResourceAsStream("medivh.version")
        resourceStream?.bufferedReader()?.use { reader ->
            reader.readLine()?.let { version ->
                project.extensions.extraProperties["medivhVersion"] = version
            }
        }

        project.afterEvaluate {
            //   todo  build src auto 
            val byteBuddyVersion = "1.15.1"

            val medivhVersion = project.extensions.extraProperties["medivhVersion"]
            project.dependencies.add("implementation", "tech.medivh:medivh-api:${medivhVersion}")
            project.dependencies.add("testImplementation", "net.bytebuddy:byte-buddy-agent:$byteBuddyVersion")
            project.dependencies.add("testImplementation", "net.bytebuddy:byte-buddy:$byteBuddyVersion")
            val copyAgent = project.tasks.register("copyAgent", CopyAgentTask::class.java, medivhVersion)

            project.tasks.withType(Test::class.java) { test ->
                test.dependsOn("copyAgent")
                if (medivhExtension.skip()) {
                    val warnMessage = """
                        Medivh Warn:no package to include,please use medivh.include("your package name").
                        example in build.gradle.kts:
                        medivh {
                            include("org.example")
                        }
                    """.trimIndent()
                    project.logger.warn(warnMessage)
                    return@withType
                }
                test.jvmArgs("-javaagent:${copyAgent.get().outputFile}=${medivhExtension.toParams()}")
            }
        }
    }
}
