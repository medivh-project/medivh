val fastjson2Version: String by medivhVersion
val byteBuddyVersion: String by medivhVersion
val mockitoKotlinVersion: String by medivhVersion

plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    insertAgent
    id("tech.medivh.plugin.publisher") version "1.0.0"
}

dependencies {
    implementation(project(":medivh-api"))
    implementation("net.bytebuddy:byte-buddy:$byteBuddyVersion")
    implementation("com.alibaba.fastjson2:fastjson2:$fastjson2Version")
    testImplementation("org.mockito.kotlin:mockito-kotlin:${mockitoKotlinVersion}")
}



medivhPublisher {
    pom {
        url = "https://github.com/medivh-project/medivh"
        licenses {
            license {
                name = "GPL-3.0 license"
                url = "https://www.gnu.org/licenses/gpl-3.0.txt"
            }
        }
        developers {
            developer {
                id = "gxz"
                name = "Xuan-Zhang Gong"
                email = "gongxuanzhangmelt@gmail.com"
            }
        }
        scm {
            connection = null
            url = "https://github.com/medivh-project/medivh"
        }
    }
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
