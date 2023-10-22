import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
    id("org.jetbrains.compose") version "1.5.2"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(kotlin("reflect"))
}

//compose.desktop {
//    application {
//        mainClass = "MainKt"
//    }
//}

//tasks.withType<KotlinCompile> {
//    kotlinOptions {
//        freeCompilerArgs = listOf(
//            "-Xcontext-receivers",
////            "-Xuse-k2",
//            "-Xbackend-threads=4",
//        )
//        jvmTarget = "11"
//
//    }
//}
//
