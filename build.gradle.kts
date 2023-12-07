plugins {
    id("java")
}

group = "dev.drtheo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":woid"))
    compileOnly("org.ow2.asm:asm-util:9.0")
}