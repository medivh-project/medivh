val asmVersion: String by medivhVersion
val jacksonVersion: String by medivhVersion
plugins {
    kotlin("jvm")
    `maven-publish`
    `java-gradle-plugin`
    insertAgent
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    api(project(":medivh-api"))
    implementation("org.ow2.asm:asm:$asmVersion")
    implementation("net.bytebuddy:byte-buddy:1.15.0")
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
