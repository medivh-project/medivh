package com.gongxuanzhang.medivh.core

import java.io.PrintStream
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class HasAnnotationMethodVisitor(api: Int, private val annotationClass: Class<out Annotation>) : MethodVisitor(api) {

    var hasTargetAnnotation = false


    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        if (descriptor == Type.getType(annotationClass).descriptor) {
            hasTargetAnnotation = true
            return null
        }
        return super.visitAnnotation(descriptor, visible)
    }
}
