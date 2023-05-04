package app.smartalerts

import app.smartalerts.routing.contact
import app.smartalerts.routing.event
import app.smartalerts.routing.eventContacts
import app.smartalerts.routing.user
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing

fun main() {
  embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
    .start(wait = true)
}

fun Application.module() {
  configureSerialization()
  configureRouting()
}

fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json()
  }
}

private fun Application.configureRouting() {
  routing {
    contact()
    event()
    eventContacts()
    user()
  }
}
