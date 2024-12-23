package tech.medivh.plugin.gradle.kotlin

import com.alibaba.fastjson2.JSON
import tech.medivh.core.i18n
import tech.medivh.core.jfr.JfrEventClassifier
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class MedivhReporter(private val medivhExtension: MedivhExtension) {


    fun report() {
        val dir = File(medivhExtension.properties.reportDir)
        val reportZip = dir.parentFile.parentFile.parentFile.resolve("medivh-report.zip")
        val reportDir = dir.resolve("report/")
        unzip(reportZip, reportDir)
        generateTraceData(reportDir)
        val indexHtml = reportDir.resolve("index.html")
        //  in windows, the path is like "C:\Users\xxx" but idea console can't recognize it is a file path
        println(i18n(medivhExtension.properties.language, "tip.seeReport", indexHtml.absolutePath.replace("\\", "/")))
    }


    private fun generateTraceData(dir: File) {
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val classifier = JfrEventClassifier(File("${medivhExtension.properties.reportDir}/medivh.jfr"))
        val testCaseReportList = classifier.classify()
        val jsFile = dir.resolve("medivh.js")
        jsFile.writeText("const testCasesData = ")
        jsFile.appendText(JSON.toJSONString(testCaseReportList))
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
