package app.smartalerts.routing

import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

fun Routing.eventContacts() {
  post("/event-contacts/{event}") {

  }
}
