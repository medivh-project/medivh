package tech.medivh.plugin.gradle.kotlin

import com.alibaba.fastjson2.JSONArray
import tech.medivh.core.env.RunningMode
import tech.medivh.core.i18n
import tech.medivh.core.jfr.*
import tech.medivh.core.reporter.TagMethod
import tech.medivh.core.reporter.TagMethodLogFileReader
import tech.medivh.core.statistic.JfrStatistic
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class MedivhReporter(private val medivhExtension: MedivhExtension) {

    private val analyzer = JfrAnalyzer()

    private val statistic: MutableMap<JfrMethod, JfrStatistic> = hashMapOf()


    fun report() {
        if (medivhExtension.properties.runningMode == RunningMode.NORMAL) {
            normalReport()
            return
        }
        deepReport()
    }

    private fun deepReport() {
        val jfrFile = File("${medivhExtension.properties.reportDir}/medivh.jfr")
        val reverser = JfrReverser(jfrFile)
        reverser.writeReverse()
        val threadTree = mutableMapOf<JfrThread, ThreadEventTreeBuilder>()
        reverser.readReverse { thread, node ->
            threadTree.computeIfAbsent(thread) {
                ThreadEventTreeBuilder(it)
            }.processNode(node)
        }
        analyzer.analysis(jfrFile) {
            statistic.merge(it.method, JfrStatistic(it), JfrStatistic::merge)
        }
//        val jsonArray = JSONArray(statistic.values)
//        JsGenerator(medivhExtension).generateJs(jsonArray.toJSONString())
//        val indexHtml = File(medivhExtension.properties.reportDir).resolve("index.html")
//        println(i18n(medivhExtension.properties.language, "tip.seeReport", indexHtml.absolutePath))
    }

    private fun normalReport() {
        val tagMethodMap = mutableMapOf<String, TagMethod>()
        TagMethodLogFileReader(File(medivhExtension.properties.reportDir).resolve(TagMethod.FILE_NAME)).read {
            tagMethodMap["${it.className}#${it.methodName}"] = it
        }
        val jfrFile = File("${medivhExtension.properties.reportDir}/medivh.jfr")
        analyzer.analysis(jfrFile) {
            statistic.merge(it.method, JfrStatistic(it), JfrStatistic::merge)
        }
        val dir = File(medivhExtension.properties.reportDir)
        val reportZip = dir.parentFile.parentFile.parentFile.resolve("medivh-report.zip")
        val reportDir = dir.resolve("report/")
        unzip(reportZip, reportDir)
        val jsonArray = JSONArray(statistic.values.map {
            it.apply {
                it.expectTime = tagMethodMap[it.methodName]?.expectTime ?: 0
            }
        })
        JsGenerator(medivhExtension).generateJs(jsonArray.toJSONString())
        val indexHtml = reportDir.resolve("index.html")
        //  in windows, the path is like "C:\Users\xxx" but idea console can't recognize it is a file path
        println(i18n(medivhExtension.properties.language, "tip.seeReport", indexHtml.absolutePath.replace("\\", "/")))
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
