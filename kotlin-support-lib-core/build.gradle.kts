import java.util.Date

plugins {
  kotlin("jvm")
  id("com.jfrog.bintray") version ("1.8.4")
  maven
  `maven-publish`
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))

  testImplementation(kotlin("test"))
  testImplementation(kotlin("reflect"))
  testImplementation("org.junit.jupiter", "junit-jupiter-api", Versions.jUnit5)
  testImplementation("org.junit.jupiter", "junit-jupiter-params", Versions.jUnit5)
  testImplementation("org.amshove.kluent", "kluent", Versions.kluent)

  testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", Versions.jUnit5)
}

tasks.withType<Test> {
  useJUnitPlatform()
}

val sourceSets = project.properties["sourceSets"] as SourceSetContainer
val mainSources = sourceSets.getByName("main")

val sourcesJar by tasks.creating(Jar::class) {
  archiveClassifier.set("sources")
  from(mainSources.allSource)
}

bintray {
  user = if (project.hasProperty("bintrayUser")) project.property("bintrayUser").toString() else ""
  key = if (project.hasProperty("bintrayApiKey")) project.property("bintrayApiKey").toString() else ""
  setPublications("LightspotsBintray")
  pkg = PackageConfig()
  pkg.repo = "kotlin"
  pkg.name = name
  pkg.userOrg = "lightspots"
  pkg.setLicenses("MIT")
  pkg.vcsUrl = "https://git.leber-lfbg.ch/Grisu118/kotlin-wrapper.git"
  pkg.version = VersionConfig()
  pkg.version.name = project.version.toString()
  pkg.version.desc = ""
  pkg.version.released = Date().toString()
  pkg.version.vcsTag = project.version.toString()
}

publishing {
  publications {
    register("LightspotsBintray", MavenPublication::class) {
      from(components["java"])
      artifact(sourcesJar)
    }
  }
}
