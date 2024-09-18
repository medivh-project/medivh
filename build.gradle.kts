import com.gongxuanzhang.medivh.build.Versions

plugins{
    kotlin("jvm")
}

val versionsFile: String by properties
val versions = Versions(file(versionsFile))

allprojects {
    
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "com.gongxuanzhang"
    version = "0.0.1"
    
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
        jvmToolchain(8)
    }
    
    extensions.add("versions", versions)
}



