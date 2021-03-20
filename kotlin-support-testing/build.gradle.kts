plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("com.jfrog.bintray") version ("1.8.5")
    maven
    `maven-publish`
}

version = "0.1.0"

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
    withSourcesJar()
}

val kdocJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Creates KDoc"
    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
}

tasks.jar.configure {
    finalizedBy(kdocJar)
}

publishing {
    publications {
        create<MavenPublication>("LightspotsBintray") {
            from(components["java"])
            artifact(kdocJar)
        }
    }
}

bintray {
    user = if (project.hasProperty("bintrayUser")) project.property("bintrayUser").toString() else ""
    key = if (project.hasProperty("bintrayApiKey")) project.property("bintrayApiKey").toString() else ""
    setPublications(*publishing.publications.names.toTypedArray())

    with(pkg) {
        repo = "kotlin"
        name = project.name
        userOrg = "lightspots"
        setLicenses("MIT")
        vcsUrl = "https://github.com/Lightspots/kotlin-support-lib.git"

        with(version) {
            name = project.version.toString()
            vcsTag = project.version.toString()
        }
    }
}

