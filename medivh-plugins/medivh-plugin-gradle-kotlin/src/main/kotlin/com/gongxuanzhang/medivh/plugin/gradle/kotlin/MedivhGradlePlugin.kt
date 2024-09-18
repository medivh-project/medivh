package com.gongxuanzhang.medivh.plugin.gradle.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class MedivhGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.dependencies.add("implementation", "com.gongxuanzhang:medivh-api:0.0.1")
        
        val medivh = project.tasks.register("medivh", MedivhTask::class.java).get()
        project.tasks.named("compileKotlin").configure {
            it.finalizedBy(medivh)
        }
    }
}
