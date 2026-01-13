plugins {
    kotlin("jvm") version "1.9.21"
    application
    id("com.google.protobuf") version "0.9.4"
}

group = "com.tomtom.sdk.tools"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:3.25.1")
    implementation("com.google.protobuf:protobuf-kotlin:3.25.1")
    implementation("com.squareup:kotlinpoet:1.15.3")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
    implementation(kotlin("stdlib"))

    // Test dependencies
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("io.mockk:mockk:1.13.8")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("com.tomtom.sdk.tools.bindingsgenerator.MainKt")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}

