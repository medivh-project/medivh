package tech.medivh.core

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
object TimeReporter {

    private val threadPool = ThreadPoolExecutor(
        1, 1,
        0, TimeUnit.SECONDS,
        ArrayBlockingQueue(8196 * 4),
        MedivhThreadFactory
    )

    //  todo  init capacity by scan method count 
    private val startMap = HashMap<String, MethodStartRecord>(8196)

    private val statisticMap = HashMap<String, MethodStatistic>(8196)

    fun start(token: String) {
        threadPool.submit {
            startMap[token] = MethodStartRecord(token)
        }
    }

    fun end(token: String) {
        threadPool.submit {
            startMap.remove(token)?.let {
                val cost = System.currentTimeMillis() - it.startTime
                val methodStatistic = MethodStatistic(token, 1, cost, cost, cost)
                statisticMap.merge(token, methodStatistic) { old, new ->
                    old.invokeCount += new.invokeCount
                    old.totalCost += new.totalCost
                    old.maxCost = old.maxCost.coerceAtLeast(new.maxCost)
                    old.minCost = old.minCost.coerceAtMost(new.minCost)
                    old
                }
            }
        }
    }


    fun generateHtml(dir: File) {
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val medivhDir = dir.parentFile.parentFile.parentFile
        unzip(medivhDir.resolve("report.zip"), dir)
        dir.resolve("js").resolve("medivh.js").writeText(generateMedivhJsContent())
        val reportHtml = dir.resolve("index.html")
        println("you can open ${reportHtml.absolutePath} to see the report")
    }

    private fun generateMedivhJsContent(): String {
        val json = generateJson()
        return """
            function jsonData() {
                return $json;
            }
        """.trimIndent()
    }

    private fun unzip(zipFile: File, targetDir: File) {
        ZipInputStream(FileInputStream(zipFile)).use { zis ->
            var entry: ZipEntry? = zis.nextEntry
            while (entry != null) {
                val newFile = File(targetDir, entry.name)
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

    private fun generateJson(): String {
        val result = statisticMap.values
        if (result.isEmpty()) {
            return "[]"
        }
        return if (result.size == 1) {
            "[${result.first().jsonLine()}]"
        } else {
            result.joinToString(",", "[", "]") { it.jsonLine() }
        }
    }

}


object MedivhThreadFactory : ThreadFactory {

    override fun newThread(r: Runnable): Thread {
        return Thread(r, "Medivh-Thread")
    }

}
