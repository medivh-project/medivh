package com.gongxuanzhang.medivh.core

import com.gongxuanzhang.medivh.api.DebugTime
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

/**
 *
 * @author gongxuanzhangmelt@gmail.com
 */
class TimeReportVisitor(api: Int, classVisitor: ClassVisitor) : ClassVisitor(api, classVisitor) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return HasAnnotationMethodVisitor(api,DebugTime::class.java)
    }
}
