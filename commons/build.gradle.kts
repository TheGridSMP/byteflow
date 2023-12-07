plugins {
    id("java")
}

group = "dev.drtheo.byteflow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":")) // base
    compileOnly("net.bytebuddy:byte-buddy-agent:1.14.10") // agent mixer

    compileOnly("com.google.code.gson:gson:2.8.8") // json config
    compileOnly("org.yaml:snakeyaml:1.28") // yaml config
}