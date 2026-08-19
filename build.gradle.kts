plugins {
    kotlin("jvm") version "2.3.20"
    application
}

group = "se.oakbeach"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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
