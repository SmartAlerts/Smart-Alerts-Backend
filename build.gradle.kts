plugins {
  alias(libs.plugins.kotlin)
  alias(libs.plugins.ktor)
  alias(libs.plugins.kotlinx.serialization)
  alias(libs.plugins.sqldelight)
}

group = "app.smartalerts"
version = "0.0.1"

application {
  mainClass.set("app.smartalerts.ApplicationKt")
}


dependencies {
  // Ktor
  implementation(libs.bundles.ktor)

  // Koin
  implementation(libs.bundles.koin)

  // Configurations
  implementation(libs.hoplite)

  // Database
  implementation(libs.mariadb.client)
  implementation(libs.h2)
  implementation(libs.jdbc.driver)

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

sqldelight {
  databases {
    create("Database") {
      packageName.set("app.smartalerts")
      dialect("app.cash.sqldelight:mysql-dialect:${libs.versions.sqldelight.get()}")
      migrationOutputDirectory.set(file("$buildDir/resources/main/migrations"))
      deriveSchemaFromMigrations.set(true)
    }
  }
}
