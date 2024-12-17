val byteBuddyVersion: String by medivhVersion
val fastjson2Version: String by medivhVersion
val mockitoKotlinVersion: String by medivhVersion

plugins {
    kotlin("jvm")
    id("com.gradle.plugin-publish")
    `java-gradle-plugin`
}



dependencies {
    implementation(project(":medivh-core"))
    implementation("net.bytebuddy:byte-buddy:$byteBuddyVersion")
    implementation("net.bytebuddy:byte-buddy-agent:$byteBuddyVersion")
    implementation("com.alibaba.fastjson2:fastjson2:$fastjson2Version")
    implementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testImplementation("org.mockito.kotlin:mockito-kotlin:${mockitoKotlinVersion}")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

@Suppress("UnstableApiUsage")
gradlePlugin {
    website = "https://medivh.tech"
    vcsUrl = "https://github.com/medivh-project/medivh.git"
    plugins {
        create("medivh") {
            id = "tech.medivh.plugin.gradle"
            implementationClass = "tech.medivh.plugin.gradle.kotlin.MedivhGradlePlugin"
            group = "tech.medivh"
            version = project.version.toString()
            displayName = "function monitor"
            description = "monitor your function without intrusion"
            tags = setOf("test", "monitor", "function")
        }
    }
}
