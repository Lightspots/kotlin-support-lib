plugins {
    kotlin("jvm") version "1.5.0" apply false
    id("org.jetbrains.dokka") version "1.4.32" apply false
}

allprojects {
    group = "ch.lightspots.it"
    version = "0.4.0"

    repositories {
        mavenCentral()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<org.jetbrains.dokka.gradle.DokkaTask> {
        outputDirectory.set(File(buildDir, "javadoc"))
    }
}
