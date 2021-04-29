plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    // BOM dependency
    testImplementation(platform("io.strikt:strikt-bom:${Versions.strikt}"))
    testImplementation("io.strikt", "strikt-core")
    testImplementation("io.strikt", "strikt-mockk")
    testImplementation("io.strikt", "strikt-java-time")

    testImplementation("org.junit.jupiter", "junit-jupiter-api", Versions.jUnit5)
    testImplementation("org.junit.jupiter", "junit-jupiter-params", Versions.jUnit5)

    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", Versions.jUnit5)
    testRuntimeOnly("org.slf4j", "slf4j-simple", Versions.slf4j)
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
