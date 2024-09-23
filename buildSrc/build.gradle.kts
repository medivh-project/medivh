plugins {
    kotlin("jvm") version "2.0.20"
    `java-gradle-plugin`
}

repositories {
    mavenLocal()
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("jarAgain") {
            id = "jarAgain"
            implementationClass = "tech.medivh.build.JarAgainPlugin"
        }
    }
}
