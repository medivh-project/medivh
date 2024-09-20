val asmVersion: String by versions

plugins {
    id("com.gradle.plugin-publish") version "1.2.1"
    jarAgain
}

dependencies {
    implementation(project(":medivh-api"))
    implementation("org.ow2.asm:asm:$asmVersion")
    implementation("net.bytebuddy:byte-buddy:1.15.0")
}


tasks.jar {
    manifest {
        attributes(
            "Premain-Class" to "com.gongxuanzhang.medivh.core.Medivh",
            "Manifest-Version" to "1.0",
            "Can-Redefine-Classes" to "true",
            "Can-Retransform-Classes" to "true"
        )
    }
}
