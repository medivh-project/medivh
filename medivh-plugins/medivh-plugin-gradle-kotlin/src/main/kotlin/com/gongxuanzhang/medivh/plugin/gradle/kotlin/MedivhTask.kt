package com.gongxuanzhang.medivh.plugin.gradle.kotlin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class MedivhTask : DefaultTask() {


    @TaskAction
    fun enhanceCompileKotlin() {
        println("MedivhTask enhanceCompileKotlin")
    }


    companion object {
        const val BYTE_CODE_EXTENSION = "class"
    }


}
