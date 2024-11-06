package tech.medivh.core

import tech.medivh.core.env.MedivhContext


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class MedivhJsGenerator(private val context: MedivhContext) {


    fun generateJs() {
        val medivhJs = context.reportDir().resolve("report/").resolve("js/").resolve("medivh.js")
        medivhJs.parentFile.mkdirs()
        val json = context.mode().timeReport.generateJsonString()
        medivhJs.writeText(generateMedivhJsContent(json))
        medivhJs.appendText("\n${defaultLanguageFunction()}")
    }


    private fun generateMedivhJsContent(json: String): String {
        return """
            function jsonData() {
                return $json;
            }
        """.trimIndent()
    }

    private fun defaultLanguageFunction(): String {
        return """
            function defaultLanguage() {
                return '${context.language().name}';
            }
        """.trimIndent()
    }

}
