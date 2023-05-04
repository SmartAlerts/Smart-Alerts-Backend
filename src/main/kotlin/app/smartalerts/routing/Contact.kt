package app.smartalerts.routing

import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Routing.contact() {
  post("/contact") {

  }
  get("/contact/{user}") {

  }
}
