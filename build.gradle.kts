plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.dokka) apply false
}

allprojects {
    group = "ch.lightspots.it"
    version = "0.4.0"

    repositories {
        mavenCentral()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    tasks.withType<org.jetbrains.dokka.gradle.DokkaTask> {
        outputDirectory.set(File(buildDir, "javadoc"))
    }
}
