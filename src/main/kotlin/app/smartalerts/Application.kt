package app.smartalerts

import app.smartalerts.routing.contact
import app.smartalerts.routing.event
import app.smartalerts.routing.eventContacts
import app.smartalerts.routing.user
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlinx.serialization.json.JsonNamingStrategy.Builtins
import org.koin.ktor.plugin.Koin

fun main() {
  embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
  configureKoin()
  configureSerialization()
  configureRouting()
}

fun Application.configureKoin() {
  install(Koin) {
    modules(KtorModule)
  }
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureSerialization() {
  install(ContentNegotiation) {
    json(Json {
      // FIXME untested
      namingStrategy = JsonNamingStrategy.SnakeCase
    })
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


