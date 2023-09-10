plugins {
    id("java")
}

group = "dev.drtheo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.0")
    implementation("org.ow2.asm:asm-tree:9.0")
    implementation("org.ow2.asm:asm-util:9.0")

    implementation("org.javassist:javassist:3.29.2-GA")
}