package com.gongxuanzhang.medivh.core

import java.io.PrintStream
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class EnhanceVisitor : MethodVisitor(Opcodes.ASM9) {

    override fun visitCode() {
        super.visitCode()
        val printStream = Type.getType(PrintStream::class.java)
        val objectDescriptor = Type.getType(Object::class.java).descriptor
        mv.visitLdcInsn("Medivh!")
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", printStream.descriptor)
        mv.visitInsn(Opcodes.SWAP)
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            printStream.internalName,
            "println",
            "($objectDescriptor)V",
            false
        )
    }
}
