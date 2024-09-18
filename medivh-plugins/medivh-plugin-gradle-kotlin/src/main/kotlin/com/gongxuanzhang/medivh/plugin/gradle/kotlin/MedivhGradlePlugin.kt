package com.gongxuanzhang.medivh.plugin.gradle.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class MedivhGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val medivh = project.tasks.register("medivh", Medivh::class.java).get()
        project.tasks.named("compileKotlin").configure {
            it.finalizedBy(medivh)
        }
    }
}
