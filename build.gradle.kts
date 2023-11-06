
plugins {
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.compose") version "1.5.10"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.animationGraphics)
    implementation(compose.animation)
    implementation(compose.ui)
    implementation(compose.runtime)
    implementation(compose.runtimeSaveable)
    implementation(compose.foundation)
    implementation(compose.uiTooling)
//    implementation(compose)

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
