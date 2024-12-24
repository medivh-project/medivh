package tech.medivh.core.jfr

import tech.medivh.core.statistic.TestCaseRecord
import java.io.File


/**
 *
 * if jfr file too long , we can't sort it in memory.
 *
 *
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class ExternalClassifier : JfrClassifier {

    override fun classify(jfrFile: File): List<TestCaseRecord> {
        externalWrite(jfrFile)
        return externalRead(jfrFile.parentFile.resolve(SORT_TEMP_DIR))
    }

    /**
     * write external file support sort.
     * every thread has a file.
     */
    private fun externalWrite(jfrFile: File) {
        val testCaseThreadPool = mutableMapOf<String, MutableMap<Long, ExternalSortedWriter>>()
        JfrAnalyzer(jfrFile).analysisEvent { event ->
            val testCase = event.getString("testCase")
            val node = FlameNode.fromMethodInvokeEvent(event)
            val threadPool = testCaseThreadPool.computeIfAbsent(testCase) {
                mutableMapOf()
            }
            threadPool.computeIfAbsent(event.thread.javaThreadId) {
                ExternalSortedWriter(
                    //   /sort_temp/testCaseName/threadId/
                    jfrFile.parentFile.resolve(SORT_TEMP_DIR).resolve(testCase)
                        .resolve(event.thread.javaThreadId.toString()),
                    JfrThread(event.thread)
                )
            }.append(node)
        }
        testCaseThreadPool.values.forEach {
            it.values.forEach { writer ->
                writer.flush()
            }
        }
    }

    private fun externalRead(sortTempDir: File): List<TestCaseRecord> {
        val testCaseDirs = sortTempDir.listFiles { file -> file.isDirectory } ?: return emptyList()
        return testCaseDirs.map { testCaseDir ->
            val threadRecords = testCaseDir.listFiles { file -> file.isDirectory }!!
                .map { ExternalSortedReader(it).read() }.toList()
            TestCaseRecord(testCaseDir.name).apply {
                threads.addAll(threadRecords)
            }
        }.toList()
    }

    companion object {
        const val SORT_TEMP_DIR = "sort_temp"
    }
}
