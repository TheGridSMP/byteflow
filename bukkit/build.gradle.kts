plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.drtheo.byteflow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/nms/")
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.18-R0.1-SNAPSHOT")

    // include byteflow core
    implementation(project(":")) {
        exclude("org.javassist")
    }
}

tasks.withType<ProcessResources> {
    val props = mapOf("version" to version)
    inputs.properties(props)

    filesMatching("plugin.yml") {
        expand(props)
    }
}