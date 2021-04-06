import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.4.32"
    id("org.jetbrains.compose") version "0.4.0-build179"
}

group = "ilio"
version = "1.0"

sourceSets {
    main {
        java {
            srcDir("src/main/kotlin")
        }
    }
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir("src/support/kotlin")
        }
    }
}

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    // Compose
    implementation(compose.desktop.currentOs)
//    implementation("org.jetbrains.compose.ui:ui-desktop:0.4.0-idea-preview-build57")
    implementation("com.arkivanov.decompose:decompose:0.2.1")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.2.1")

    // Common lib
    implementation("commons-io:commons-io:2.8.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("com.google.guava:guava:30.1.1-jre")

    // Http
    implementation("org.jodd:jodd-http:6.0.6")

    // Baidu OCR
    implementation("com.baidu.aip:java-sdk:4.15.6")

    // Json Parser
    implementation("com.beust:klaxon:5.5")

    // Mock
    testImplementation("io.mockk:mockk:1.11.0")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "ilio.translation.MainKt"
        nativeDistributions {
            javaHome = findTargetJdk()
            println("use java_home: $javaHome")

            targetFormats(TargetFormat.Dmg)
            packageName = "translation"
            packageVersion = "1.0.0"
        }
    }
}

fun findTargetJdk(): String {
    val os: OperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()
    if (os.isMacOsX) {
        var process: Process ?= null
        try {
            process = Runtime.getRuntime().exec("/usr/libexec/java_home -v 11+ -v 16-")
            return String(process.inputStream.readBytes()).replace("\n", "")
        } finally {
            process?.destroyForcibly()
        }
    }
    return ""
}
