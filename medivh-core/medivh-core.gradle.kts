val asmVersion: String by versions

plugins{
    id("com.gradle.plugin-publish") version "1.2.1"
}

dependencies {
    implementation(project(":medivh-api"))
    implementation("org.ow2.asm:asm:$asmVersion")
}
