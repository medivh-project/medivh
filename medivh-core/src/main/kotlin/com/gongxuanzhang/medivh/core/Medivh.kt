package com.gongxuanzhang.medivh.core

import java.io.File
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class Medivh(private val classFile: File, medivhCache: MedivhCache) {

    init {
        check(classFile.exists() || classFile.isFile) {
            "class file [${classFile.path}] not found"
        }
    }

    var shouldRewrite = false

    fun execute() {
        val classReader = ClassReader(classFile.readBytes())
        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
        ClassRewriter(this, classWriter).let {
            classReader.accept(it, 0)
        }
        if (shouldRewrite) {
            classFile.writeBytes(classWriter.toByteArray())
            println("rewrite class file ${classReader.className}")
        }
    }

}
