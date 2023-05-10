package app.smartalerts.routing

import app.smartalerts.serialization.LocalDateTimeSerializer
import app.smartalerts.service.EventService
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

fun Routing.event() {
  val eventService by inject<EventService>()
  post("/event") {
    val request = call.receive<CreateEventRequest>()
    eventService.create(request.smartUser, request.description, request.dateTime, request.isCancelled).fold(
      // FIXME return message
      onSuccess = { call.respond(it.id) },
      onFailure = { call.respond(BadRequest) }
    )
  }
}

@Serializable
data class CreateEventRequest(
  val smartUser: String,
  val description: String,
  @Serializable(LocalDateTimeSerializer::class) val dateTime: LocalDateTime,
  val isCancelled: Boolean = false
)
