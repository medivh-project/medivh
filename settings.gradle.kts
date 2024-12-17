rootProject.name = "medivh"

pluginManagement {
    val buildKotlinVersion: String by settings
    val publishVersion: String by settings

    plugins {
        kotlin("jvm") version buildKotlinVersion apply false
        id("com.gradle.plugin-publish") version publishVersion apply false
    }

}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(":medivh-api")
include(":medivh-core")
include(":medivh-plugins")
include(":medivh-plugins:medivh-plugin-gradle-kotlin")
include(":medivh-junit-extension")

rootProject.children.forEach { it.configureBuildScriptName() }

fun ProjectDescriptor.configureBuildScriptName() {
    buildFileName = "${name}.gradle.kts"
    children.forEach { it.configureBuildScriptName() }
}

