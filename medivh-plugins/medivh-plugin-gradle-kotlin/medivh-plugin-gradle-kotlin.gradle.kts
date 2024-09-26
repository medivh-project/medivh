val byteBuddyVersion: String by medivhVersion
val jacksonVersion: String by medivhVersion


plugins {
    kotlin("jvm")
    id("com.gradle.plugin-publish")
    `java-gradle-plugin`
}



dependencies {
    api(project(":medivh-core"))
    implementation("net.bytebuddy:byte-buddy:$byteBuddyVersion")
    implementation("net.bytebuddy:byte-buddy-agent:$byteBuddyVersion")
}


gradlePlugin {
    plugins {
        create("medivh") {
            id = "tech.medivh.plugin.gradle"
            implementationClass = "tech.medivh.plugin.gradle.kotlin.MedivhGradlePlugin"
            group = "tech.medivh"
            version = project.version.toString()
            displayName = "function monitor"
            description = "monitor your function without intrusion"
        }
    }
}
