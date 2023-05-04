package app.smartalerts.service

import app.smartalerts.migrations.Event
import app.smartalerts.queries.EventQueries
import java.time.LocalDateTime
import java.util.UUID
import java.util.UUID.randomUUID
import app.smartalerts.migrations.Event as EventEntity

class EventService(
  private val eventQueries: EventQueries,
) {

  fun create(
    smartUser: String,
    description: String,
    dateTime: LocalDateTime,
    isCancelled: Boolean = false,
  ): Result<EventEntity> {
    val event = Event(smartUser, description, dateTime, isCancelled, randomUUID().toString())
    return runCatching {
      eventQueries.insert(event)
    }.map { event }
  }
}
