package tech.medivh.core

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.instrument.Instrumentation
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.asm.Advice
import net.bytebuddy.description.annotation.AnnotationDescription
import net.bytebuddy.matcher.ElementMatchers
import tech.medivh.api.DebugTime
import tech.medivh.core.interceptor.MultiThreadInterceptor
import tech.medivh.core.interceptor.NormalInterceptor


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object Medivh {

    @JvmStatic
    fun premain(agentArgs: String?, inst: Instrumentation) {

        val context = MedivhContext(agentArgs)

        Runtime.getRuntime().addShutdownHook(Thread {
            val timeReport = context.mode().timeReport
            // dir = /build/medivh/reports/time/uuid
            val dir = context.targetDir()
            val reportZip = dir.parentFile.parentFile.parentFile.resolve("medivh-report.zip")
            val reportDir = dir.resolve("report/")
            reportDir.mkdirs()
            unzip(reportZip, reportDir)
            MedivhJsGenerator(context).generateJs()
            reportDir.resolve(timeReport.htmlTemplateName()).copyTo(reportDir.resolve("index.html.temp")).apply {
                this.parentFile.listFiles { file -> file.extension == "html" }?.forEach {
                    check(it.delete()) { i18n(context.language(), "error.fileDeleteFailed", it.path) }
                }
                val indexHtml = reportDir.resolve("index.html")
                check(this.renameTo(indexHtml)) { i18n(context.language(), "error.fileRenameFailed", indexHtml.path) }
                println(i18n(context.language(), "tip.seeReport", indexHtml.absolutePath))
            }
        })

        val mode = context.mode()
        val advice = when (mode) {
            MedivhMode.NORMAL -> Advice.to(NormalInterceptor::class.java)
            MedivhMode.MULTI_THREAD -> Advice.to(MultiThreadInterceptor::class.java)
        }

        val debugTimeName = DebugTime::class.java.name
        AgentBuilder.Default().type(context.includeMatchers())
            .transform { builder, desc, _, _, _ ->
                desc.declaredMethods.forEach { method ->
                    val debugTime = method.asDefined().declaredAnnotations.find {
                        it.annotationType.name == debugTimeName
                    }
                    debugTime?.let {
                        val desc = resolveDebugTime(it)
                        mode.timeReport.setup(MethodSetup(MethodToken.fromMethodDescription(method), desc))
                    }
                }
                builder.method(ElementMatchers.isAnnotatedWith(DebugTime::class.java))
                    .intercept(advice)
            }.installOn(inst)

    }


    private fun resolveDebugTime(debugTime: AnnotationDescription): DebugTimeDesc {
        return DebugTimeDesc(debugTime.getValue("expectTime").resolve() as Long)
    }

    private fun unzip(zipFile: File, reportDir: File) {
        if (reportDir.resolve("report/js").exists()) {
            return
        }
        ZipInputStream(FileInputStream(zipFile)).use { zis ->
            var entry: ZipEntry? = zis.nextEntry
            while (entry != null) {
                val newFile = File(reportDir, entry.name)
                if (entry.isDirectory) {
                    newFile.mkdirs()
                } else {
                    newFile.parentFile.mkdirs()
                    FileOutputStream(newFile).use { fos ->
                        zis.copyTo(fos)
                    }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
    }
}
