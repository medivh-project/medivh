package com.gongxuanzhang.medivh.plugin.gradle.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class MedivhGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {

        println("medivh!")

        val medivhExtension = project.extensions.create("medivh", MedivhExtension::class.java)

        project.dependencies.add("implementation", "com.gongxuanzhang:medivh-api:0.0.1")
        project.dependencies.add("implementation", "net.bytebuddy:byte-buddy-agent:1.15.1")
        project.dependencies.add("implementation", "net.bytebuddy:byte-buddy:1.15.1")

        project.afterEvaluate {
            it.tasks.withType(Test::class.java) { test ->
                test.jvmArgs("-javaagent:/Users/gongxuanzhang/workSpace/java/github/medivh/medivh-core/build/libs/medivh-core-0.0.1.jar=${medivhExtension.toParams()}")
            }
        }
    }
}
