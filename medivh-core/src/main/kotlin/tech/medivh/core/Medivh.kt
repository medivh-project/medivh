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
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val medivhDir = dir.parentFile.parentFile.parentFile
            val reportDir = medivhDir.resolve("reports")
            unzip(medivhDir.resolve("report.zip"), reportDir)
            val medivhJs = dir.resolve("js").resolve("medivh.js")
            medivhJs.parentFile.mkdirs()
            medivhJs.writeText(generateMedivhJsContent(timeReport.generateJsonString()))
            val reportHtml = reportDir.resolve(timeReport.htmlTemplateName()).copyTo(dir.resolve("report.html"))
            println("you can open  file://${reportHtml.absolutePath} to see the report")
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
        if (reportDir.resolve("js").exists()) {
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
