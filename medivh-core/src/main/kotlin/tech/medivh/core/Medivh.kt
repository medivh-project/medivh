package tech.medivh.core

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.instrument.Instrumentation
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.asm.Advice
import net.bytebuddy.matcher.ElementMatchers
import tech.medivh.api.DebugTime


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object Medivh {

    @JvmStatic
    fun premain(agentArgs: String?, inst: Instrumentation) {

        val context = MedivhContext(agentArgs)

        Runtime.getRuntime().addShutdownHook(Thread {
            val timeReport = context.mode().timeReport
            val dir = context.targetDir()
            // dir = /build/medivh/reports/time/uuid
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val reportZip = dir.parentFile.parentFile.parentFile.resolve("medivh-report.zip")
            val reportDir = dir.resolve("report/")
            reportDir.mkdirs()
            unzip(reportZip, reportDir)
            val medivhJs = reportDir.resolve("js/").resolve("medivh.js")
            medivhJs.parentFile.mkdirs()
            medivhJs.writeText(generateMedivhJsContent(timeReport.generateJsonString()))
            reportDir.resolve(timeReport.htmlTemplateName()).copyTo(reportDir.resolve("index.html.temp")).apply {
                this.parentFile.listFiles { file -> file.extension == "html" }?.forEach {
                    it.delete()
                }
                val indexHtml = reportDir.resolve("index.html")
                this.renameTo(indexHtml)
                println("you can open file://${indexHtml.absolutePath} to see the report")
            }
        })

        val advice = when (context.mode()) {
            MedivhMode.NORMAL -> Advice.to(NormalInterceptor::class.java)
            MedivhMode.MULTI_THREAD -> Advice.to(MultiThreadInterceptor::class.java)
        }

        AgentBuilder.Default().type(context.includeMatchers())
            .transform { builder, _, _, _, _ ->
                builder.method(ElementMatchers.isAnnotatedWith(DebugTime::class.java))
                    .intercept(advice)
            }.installOn(inst)

    }

    private fun generateMedivhJsContent(json: String): String {
        return """
            function jsonData() {
                return $json;
            }
        """.trimIndent()
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


const val githubUrl = "https://www.github.com/medivh-project/meidvh"

fun unexpectedly(): Nothing {
    throw IllegalStateException("There seems to be a problem, please report back on $githubUrl")
}
