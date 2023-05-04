package app.smartalerts.service

import app.smartalerts.migrations.Contact
import app.smartalerts.migrations.SmartUser
import app.smartalerts.queries.ContactQueries

class ContactService(
  private val contactQueries: ContactQueries
) {
  fun addContact(user: SmartUser, contactPhone: String): Result<Contact> {
    val contact = Contact(user.phone, contactPhone)
    return runCatching { contactQueries.insert(contact) }.map { contact }
  }
}
