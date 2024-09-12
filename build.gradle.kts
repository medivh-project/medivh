import com.gongxuanzhang.medivh.build.Versions

plugins {
    kotlin("jvm")
}

val versionsFile: String by properties
val versions = Versions(file(versionsFile))

group = "com.gongxuanzhang"
version = "0.0.1"

allprojects {
    
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenLocal()
        mavenCentral()
    }

    extensions.add("versions", versions)
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
