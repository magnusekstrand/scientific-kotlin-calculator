plugins {
    kotlin("jvm") version "2.3.20"
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.20"
    id("org.jetbrains.compose") version "1.11.1"
    application
}

group = "se.oakbeach"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material)

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation(kotlin("test"))

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("se.oakbeach.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

// Separate from the `application` plugin's `run` task (console REPL) so that task's
// stdin wiring stays untouched; this launches the Compose Desktop UI entry point instead.
tasks.register<JavaExec>("runUi") {
    group = "application"
    description = "Run the Compose Desktop calculator UI"
    mainClass.set("se.oakbeach.ui.MainUiKt")
    classpath = sourceSets["main"].runtimeClasspath
}
