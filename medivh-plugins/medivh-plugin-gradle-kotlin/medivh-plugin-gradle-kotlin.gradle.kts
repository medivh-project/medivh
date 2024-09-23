val byteBuddyVersion: String by versions

plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.2.1"
}

dependencies {
    api(project(":medivh-core"))
    implementation("net.bytebuddy:byte-buddy:$byteBuddyVersion")
    implementation("net.bytebuddy:byte-buddy-agent:$byteBuddyVersion")
}


gradlePlugin {
    plugins {
        create("medivh-plugin-gradle-kotlin") {
            id = "tech.medivh.plugin.gradle.kotlin"
            implementationClass = "tech.medivh.plugin.gradle.kotlin.MedivhGradlePlugin"
        }
    }
}
