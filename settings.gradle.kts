rootProject.name = "medivh"

pluginManagement {
    val buildKotlinVersion: String by settings

    plugins {
        kotlin("jvm") version buildKotlinVersion apply false
    }

}

include("medivh-api")
include("medivh-core")

rootProject.children.forEach { it.configureBuildScriptName() }

fun ProjectDescriptor.configureBuildScriptName() {
    buildFileName = "${name}.gradle.kts"
    children.forEach { it.configureBuildScriptName() }
}