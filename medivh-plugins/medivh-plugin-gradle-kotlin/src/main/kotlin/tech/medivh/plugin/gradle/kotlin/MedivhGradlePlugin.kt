package tech.medivh.plugin.gradle.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import tech.medivh.core.i18n
import tech.medivh.core.jfr.JfrInterceptor


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class MedivhGradlePlugin : Plugin<Project> {


    override fun apply(project: Project) {

        val medivhExtension = project.extensions.create("medivh", MedivhExtension::class.java, project)

        val resourceStream = this::class.java.classLoader.getResourceAsStream("medivh.version")
        resourceStream?.bufferedReader()?.use { reader ->
            reader.readLine()?.let { version ->
                project.extensions.extraProperties["medivhVersion"] = version
            }
        }

        project.afterEvaluate {
            val byteBuddyVersion = "1.15.1"
            val fastjson2Version = "2.0.52"
            val medivhVersion = project.extensions.extraProperties["medivhVersion"]
            project.dependencies.add("implementation", "tech.medivh:medivh-api:${medivhVersion}")
            project.dependencies.add("testImplementation", "net.bytebuddy:byte-buddy-agent:$byteBuddyVersion")
            project.dependencies.add("testImplementation", "net.bytebuddy:byte-buddy:$byteBuddyVersion")
            project.dependencies.add("testImplementation", "com.alibaba.fastjson2:fastjson2:$fastjson2Version")
            val copyAgent = project.tasks.register("copyAgent", CopyAgentTask::class.java, medivhVersion)

            project.tasks.register("copyReportZip", CopyReportZipTask::class.java)

            project.tasks.withType(Test::class.java).configureEach {
                it.addTestListener(object : TestListener {
                    override fun beforeTest(descriptor: TestDescriptor) {
                        println("改成了: ${descriptor.displayName}")
                        JfrInterceptor.updateToken(descriptor.displayName)
                    }

                    override fun afterTest(descriptor: TestDescriptor, result: TestResult) {
                    }

                    override fun beforeSuite(suite: TestDescriptor) {
                    }

                    override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                    }
                })
            }

            project.tasks.withType(Test::class.java) { test ->
                test.dependsOn("copyAgent")
                test.dependsOn("copyReportZip")
                if (medivhExtension.skip()) {
                    project.logger.warn(i18n(medivhExtension.properties.language, "warn.includeSkip"))
                    return@withType
                }
                test.jvmArgs(
                    "-javaagent:${copyAgent.get().outputFile}=${medivhExtension.javaagentArgs()}",
                    "-XX:StartFlightRecording=filename=${medivhExtension.properties.reportDir}/medivh.jfr",
                    "-Xlog:jfr+startup=error"
                )
                test.doLast {
                    MedivhReporter(medivhExtension).report()
                }
            }

        }
    }
}

