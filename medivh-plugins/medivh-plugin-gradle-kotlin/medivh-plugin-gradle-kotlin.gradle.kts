plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.2.1"
}

dependencies {
    api(project(":medivh-core"))
    implementation("net.bytebuddy:byte-buddy:1.15.1")
    implementation("net.bytebuddy:byte-buddy-agent:1.15.1")

}


gradlePlugin {
    plugins {
        create("medivh-plugin-gradle-kotlin") {
            id = "com.gongxuanzhang.medivh.plugin.gradle.kotlin"
            implementationClass = "com.gongxuanzhang.medivh.plugin.gradle.kotlin.MedivhGradlePlugin"
        }
    }
}
