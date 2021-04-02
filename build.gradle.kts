import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.4.31"
    id("org.jetbrains.compose") version "0.3.2"
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
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    maven { url = uri("https://maven.aliyun.com/repository/central") }
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    maven { url = uri("https://maven.aliyun.com/repository/google") }
}

dependencies {
    implementation(compose.desktop.currentOs)

    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("org.jodd:jodd-http:6.0.6")
    implementation("com.baidu.aip:java-sdk:4.15.6")
    implementation("com.beust:klaxon:5.5")
    implementation("commons-io:commons-io:2.8.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-collections4:4.4")

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
