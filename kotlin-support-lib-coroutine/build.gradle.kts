plugins {
  kotlin("jvm")
//  id("com.jfrog.bintray") version ("1.8.4")
//  maven
//  `maven-publish`
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", Versions.kotlinCoroutines)
  implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8", Versions.kotlinCoroutines)
}
