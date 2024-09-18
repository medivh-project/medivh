package com.gongxuanzhang.medivh.core

import com.gongxuanzhang.medivh.api.DebugTime
import java.io.PrintStream
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class MethodRewriter(private val parent: Medivh, mv: MethodVisitor) : MethodVisitor(Opcodes.ASM9, mv) {

    private var shouldRewrite = false

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        if (annotationDesc.contains(descriptor)) {
            shouldRewrite = true
            parent.shouldRewrite = true
        }
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitCode() {
        if (shouldRewrite) {
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
        super.visitCode()
    }


    companion object {
        private val targetAnnotations = setOf(DebugTime::class.java)
        val annotationDesc = targetAnnotations.map { Type.getType(it).descriptor!! }.toSet()
    }

}
