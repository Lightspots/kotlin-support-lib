plugins {
    kotlin("jvm") version Versions.kotlin apply false
    id("org.jetbrains.dokka") version "1.4.10.2" apply false
}

allprojects {
    group = "ch.lightspots.it"

    repositories {
        mavenCentral()
        jcenter()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<org.jetbrains.dokka.gradle.DokkaTask> {
        outputDirectory.set(File(buildDir, "javadoc"))
    }
}
