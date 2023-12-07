plugins {
    id("java")
}

group = "woid"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.ow2.asm:asm:9.0")
}