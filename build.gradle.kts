plugins {
    id("java")
    id("checkstyle")
}

group = "ovh.neziw"
version = "1.0.0"

tasks.withType<JavaCompile> {
    options.compilerArgs = listOf("-Xlint:deprecation")
    options.encoding = "UTF-8"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

checkstyle {
    toolVersion = "10.23.0"
    maxWarnings = 0
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
//    testImplementation("com.github.MockBukkit:MockBukkit:v1.21-SNAPSHOT")
}
