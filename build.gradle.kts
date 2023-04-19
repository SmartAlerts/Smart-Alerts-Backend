plugins {
  alias(libs.plugins.kotlin)
  alias(libs.plugins.ktor)
  alias(libs.plugins.kotlinx.serialization)
}

group = "app.smartalerts"
version = "0.0.1"

application {
  mainClass.set("app.smartalerts.ApplicationKt")
}


dependencies {
  // Ktor
  implementation(libs.bundles.ktor)

  // Database
  implementation(libs.postgres)
  implementation(libs.h2)

  // Logback
  implementation(libs.logback)

  // Kotest
  testImplementation(libs.bundles.kotest)

  // TestContainers
  testImplementation(libs.bundles.test.containers)
}

tasks.withType<Test> {
  useJUnitPlatform()
}
