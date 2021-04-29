plugins {
    kotlin("jvm") version Versions.kotlin apply false
    id("org.jetbrains.dokka") version Versions.kotlin apply false
}

allprojects {
    group = "ch.lightspots.it"
    version = "0.3.0"

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
