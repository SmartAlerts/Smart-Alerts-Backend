package app.smartalerts.service

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import app.smartalerts.Database
import app.smartalerts.migrations.Event
import br.com.colman.kotest.extensions.H2Listener
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class EventServiceTest : FunSpec({
  val h2Listener = listener(H2Listener("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"))
  val driver = h2Listener.dataSource.asJdbcDriver()

  val db = Database(driver)
  beforeTest { Database.Schema.create(driver) }

  val userService = UserService(db.userQueries)
  val contactService = ContactService(db.contactQueries)
  val target = EventService(db.eventQueries, db.eventContactQueries)

  val smartUser by lazy { userService.createUser("123456").getOrThrow() }
  val contact by lazy { contactService.addContact(smartUser, "2222").getOrThrow() }
  val dateTime = LocalDateTime.now()

  test("Create Event") {
    val event = target.create(smartUser.phone, "description", dateTime)

    event shouldBeSuccess {
      it.shouldBeEqualToIgnoringFields(
        Event("123456", "description", dateTime, false, ""),
        Event::id
      )
    }
  }

  test("Associate event and contact") {
    val event = target.create(smartUser.phone, "description", dateTime).getOrThrow()
    target.associate(event, contact) shouldBeSuccess {
      it.event shouldBe event.id
      it.smart_user shouldBe smartUser.phone
      it.contact_phone shouldBe contact.contact_phone
    }
  }

})
