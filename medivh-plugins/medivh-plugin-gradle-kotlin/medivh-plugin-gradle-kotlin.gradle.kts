plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.2.1"
}

dependencies {
    api(project(":medivh-core"))
}   

gradlePlugin {
    plugins {
        create("medivh-plugin-gradle-kotlin") {
            id = "com.gongxuanzhang.medivh.plugin.gradle.kotlin"
            implementationClass = "com.gongxuanzhang.medivh.plugin.gradle.kotlin.MedivhGradlePlugin"
        }
    }
}
