package tech.medivh.plugin.gradle.kotlin

import java.io.File


/**
 * @author gxz gongxuanzhangmelt@gmail.com
 **/
class JsGenerator(private val medivhExtension: MedivhExtension) {


    fun generateJs(jsonData: String) {
        val jsFile = File(medivhExtension.properties.reportDir).resolve("report/").resolve("js/").resolve("medivh.js")
        jsFile.writeText(generateMedivhJsContent(jsonData))
        jsFile.appendText("\n${defaultLanguageFunction()}")
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
                return '${medivhExtension.properties.language.name}';
            }
        """.trimIndent()
    }

}
