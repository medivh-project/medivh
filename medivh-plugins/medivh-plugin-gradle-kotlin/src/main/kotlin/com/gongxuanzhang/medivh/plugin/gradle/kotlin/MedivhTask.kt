package com.gongxuanzhang.medivh.plugin.gradle.kotlin

import com.gongxuanzhang.medivh.core.Medivh
import com.gongxuanzhang.medivh.core.MedivhCache
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
open class MedivhTask : DefaultTask() {


    @TaskAction
    fun enhanceCompileKotlin() {
        println("MedivhTask enhanceCompileKotlin")
        val cacheFile = project.layout.buildDirectory.asFile.get().resolve("medivh-cache")
        val medivhCache = MedivhCache(cacheFile)
        project.layout.buildDirectory.asFile.get().resolve("classes/kotlin/main")
            .walkTopDown()
            .filter { it.isFile && it.extension == BYTE_CODE_EXTENSION }
            .forEach {
                Medivh(it, medivhCache).execute()
            }
        medivhCache.save()
    }


    companion object {
        const val BYTE_CODE_EXTENSION = "class"
    }


}
