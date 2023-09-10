plugins {
    id("java")
}

group = "dev.drtheo.byteflow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation(project(":"))

    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
}

tasks.withType<ProcessResources> {
    val props = mapOf("version" to version)
    inputs.properties(props)

    filesMatching("plugin.yml") {
        expand(props)
    }
}