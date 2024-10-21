plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    id("tech.medivh.plugin.publisher") version "1.0.0"
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



