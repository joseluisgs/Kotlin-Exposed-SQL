import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Variables
val exposedVersion: String by project
val h2Version: String by project

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "es.joseluisgs.dam"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    // Para manejar las fechas
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    // BBDD H2
    implementation("com.h2database:h2:$h2Version")
    // Opcionales
    // Para manejar un pool de conexions mega rápido con HikariCP (no es obligatorio)
    // https://mvnrepository.com/artifact/com.zaxxer/HikariCP
    implementation("com.zaxxer:HikariCP:5.0.1")
    // Logging
    implementation("org.slf4j:slf4j-nop:1.7.35")
    // Tersting
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}