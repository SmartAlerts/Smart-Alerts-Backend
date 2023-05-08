import io.ktor.plugin.features.JreVersion.JRE_17

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

ktor {
  docker {
    localImageName.set(project.name)
    imageTag.set("$version")
    jreVersion.set(JRE_17)
  }
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

tasks.withType<Test> {
  useJUnitPlatform()
}
