package com.gongxuanzhang.medivh.plugin.gradle.kotlin

import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class Medivh : DefaultTask() {


    @TaskAction
    fun enhanceCompileKotlin() {
        File("/Users/gongxuanzhang/Desktop/aaa.txt").createNewFile()
        project.layout.buildDirectory.asFile.get().resolve("classes/kotlin/main")
            .walkTopDown()
            .filter { it.isFile && it.extension == BYTE_CODE_EXTENSION }
            .forEach {
                println(it)
            }
    }


    companion object {
        const val BYTE_CODE_EXTENSION = "class"
    }


}
