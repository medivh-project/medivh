package com.gongxuanzhang.medivh.plugin.gradle.kotlin

import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class Medivh : DefaultTask() {


    @TaskAction
    fun enhanceCompileKotlin() {
        project.layout.buildDirectory.asFile.get().resolve("classes/kotlin/main")
            .walkTopDown()
            .filter { it.isFile && it.extension == BYTE_CODE_EXTENSION }
            .forEach {
                
            }
    }
    
    


    companion object {
        const val BYTE_CODE_EXTENSION = "class"
    }


}
