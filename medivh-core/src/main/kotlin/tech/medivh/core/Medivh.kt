package tech.medivh.core

import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.asm.Advice
import net.bytebuddy.description.annotation.AnnotationDescription
import net.bytebuddy.matcher.ElementMatchers
import tech.medivh.api.DebugTime
import tech.medivh.core.env.MedivhContext
import tech.medivh.core.jfr.JfrInterceptor
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.instrument.Instrumentation
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object Medivh {

    @JvmStatic
    fun premain(agentArgs: String?, inst: Instrumentation) {

        val properties = String(Base64.getUrlDecoder().decode(agentArgs))
        val context = MedivhContext(properties)

        Runtime.getRuntime().addShutdownHook(Thread {
            val timeReport = context.mode().timeReport
            // dir = /build/medivh/reports/time/uuid
            val dir = context.reportDir()
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
                //  in windows, the path is like "C:\Users\xxx" but idea console can't recognize it is a file path
                println(i18n(context.language(), "tip.seeReport", indexHtml.absolutePath.replace("\\", "/")))
            }
        })

        val mode = context.mode()
        val advice = Advice.to(JfrInterceptor::class.java)

        val debugTimeName = DebugTime::class.java.name
        AgentBuilder.Default().type(context.includeMatchers())
            .transform { builder, desc, _, _, _ ->
                desc.declaredMethods.forEach { method ->
                    val debugTime = method.asDefined().declaredAnnotations.find {
                        it.annotationType.name == debugTimeName
                    }
                    debugTime?.let {
                        val debugTimeDesc = resolveDebugTime(it)
                        mode.timeReport.setup(MethodSetup(MethodToken.fromMethodDescription(method), debugTimeDesc))
                    }
                }
                // builder.method(ElementMatchers.isAnnotatedWith(DebugTime::class.java))
                builder.method(ElementMatchers.any())
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

