package app.smartalerts.routing

import app.smartalerts.migrations.SmartUser
import app.smartalerts.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.ktor.ext.get

fun Routing.user(
  userService: UserService = get(),
) {
  post("/user") {
    val user = call.receive<SmartUser>()
    userService.createUser(user.phone).fold(
      onSuccess = { call.respond(it) },
      onFailure = { call.respond(BadRequest) }
    )
  }
}
