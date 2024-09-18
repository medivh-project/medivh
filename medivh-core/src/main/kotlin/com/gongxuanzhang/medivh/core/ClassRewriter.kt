package com.gongxuanzhang.medivh.core

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ClassRewriter(private val medivh: Medivh, writer: ClassWriter) : ClassVisitor(Opcodes.ASM9, writer) {

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
}
