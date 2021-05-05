plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    testImplementation(libs.bundles.strikt)

    testImplementation(libs.bundles.junit)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.slf4j.simple)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.javadoc.configure {
    dependsOn("dokkaHtml")
    setDestinationDir(File(buildDir, "dokka/html"))
}

tasks.assemble.configure {
    doLast {
        logger.lifecycle("::set-output name=version::$version")
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/lightspots/kotlin-support-lib")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
