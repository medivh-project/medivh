plugins {
    id("com.gradle.plugin-publish") version "1.2.1"
}

dependencies {
    implementation("org.slf4j:jul-to-slf4j:2.0.12")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

