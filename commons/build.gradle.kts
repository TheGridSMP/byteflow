plugins {
    id("java")
}

group = "dev.drtheo.byteflow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("org.yaml:snakeyaml:1.28")
    implementation(project(":"))
}