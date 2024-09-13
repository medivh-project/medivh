val asmVersion: String by versions

dependencies {
    implementation(project(":medivh-api"))
    implementation("org.ow2.asm:asm:$asmVersion")
}
