package com.gongxuanzhang.medivh.core

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ClassRewriter(private val medivh: Medivh, writer: ClassWriter) : ClassVisitor(Opcodes.ASM9, writer) {


    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitEnd() {
        if (medivh.shouldRewrite) {
            val av = cv.visitAnnotation(mark, true)
            av.visitEnd()
        }
        super.visitEnd()
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return MethodRewriter(medivh, mv)
    }

    companion object {
        val mark = Type.getType(MedivhIncrement::class.java).descriptor!!
    }
}
