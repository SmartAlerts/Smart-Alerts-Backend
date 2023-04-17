package app.smartalerts

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import app.smartalerts.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureRouting()
}
