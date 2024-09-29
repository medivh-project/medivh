val fastjson2Version: String by medivhVersion
val byteBuddyVersion: String by medivhVersion

plugins {
    kotlin("jvm")
    `maven-publish`
    `java-gradle-plugin`
    insertAgent
}

dependencies {
    implementation(project(":medivh-api"))
    implementation("net.bytebuddy:byte-buddy:$byteBuddyVersion")
    implementation("com.alibaba.fastjson2:fastjson2:$fastjson2Version")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.jar {
    manifest {
        attributes(
            "Premain-Class" to "tech.medivh.core.Medivh",
            "Manifest-Version" to "1.0",
            "Can-Redefine-Classes" to "true",
            "Can-Retransform-Classes" to "true"
        )
    }
}
