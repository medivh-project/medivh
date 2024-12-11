import tech.medivh.build.Versions

plugins {
    kotlin("jvm")
}

val versionsFile: String by properties
val rootVersionFile = file(versionsFile)

allprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "tech.medivh"
    version = "0.3.0"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        testImplementation(kotlin("test"))
    }

    tasks.test {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(17)
    }

    extensions.create("medivhVersion", Versions::class.java, rootVersionFile)
}



