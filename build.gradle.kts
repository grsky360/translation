import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.4.30"
    id("org.jetbrains.compose") version "0.3.1"
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

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    maven { url = uri("https://maven.aliyun.com/repository/central") }
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(compose.desktop.currentOs)

    implementation("com.google.guava:guava:30.1.1-jre")
    implementation("com.baidu.aip:java-sdk:4.15.6")
    implementation("org.jodd:jodd-http:6.0.6")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "ilio.translation.MainKt"
        nativeDistributions {
            val process = Runtime.getRuntime().exec("/usr/libexec/java_home -v 11+")
            javaHome = String(process.inputStream.readBytes()).replace("\n", "")
            println("use java_home: $javaHome")
            process.destroyForcibly()
            targetFormats(TargetFormat.Dmg)
            packageName = "translation"
            packageVersion = "1.0.0"
        }
    }
}
