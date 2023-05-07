package app.smartalerts.service

import app.smartalerts.migrations.Contact
import app.smartalerts.migrations.SmartEvent
import app.smartalerts.migrations.EventContacts
import app.smartalerts.queries.EventContactQueries
import app.smartalerts.queries.EventQueries
import java.time.LocalDateTime
import java.util.UUID.randomUUID
import app.smartalerts.migrations.SmartEvent as SmartEventEntity

class EventService(
  private val eventQueries: EventQueries,
  private val eventContactQueries: EventContactQueries
) {

  fun create(
    smartUser: String,
    description: String,
    dateTime: LocalDateTime,
    isCancelled: Boolean = false,
  ): Result<SmartEventEntity> {
    val event = SmartEvent(smartUser, description, dateTime, isCancelled, randomUUID().toString())
    return runCatching {
      eventQueries.insert(event)
    }.map { event }
  }

  fun associate(event: SmartEvent, contact: Contact, warnAfter: LocalDateTime): Result<EventContacts> {
    return runCatching {
      val eventContacts = EventContacts(event.smart_user, contact.contact_phone, warnAfter, event.id)
      eventContactQueries.insert(eventContacts)
      eventContacts
    }
  }
}
