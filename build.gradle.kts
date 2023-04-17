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
  implementation(libs.bundles.ktor)

  implementation(libs.postgres)
  implementation(libs.h2)

  implementation(libs.logback)
}
