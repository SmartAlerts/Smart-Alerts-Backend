package app.smartalerts.service

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import app.smartalerts.Database
import app.smartalerts.migrations.Event
import br.com.colman.kotest.extensions.H2Listener
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.result.shouldBeSuccess
import java.time.LocalDateTime

class EventServiceTest : FunSpec({
  val h2Listener = listener(H2Listener("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"))
  val driver = h2Listener.dataSource.asJdbcDriver()

  val db = Database(driver)
  beforeTest { Database.Schema.create(driver) }

  val userService = UserService(db.userQueries)
  val target = EventService(db.eventQueries)

  val smartUser by lazy { userService.createUser("123456").getOrThrow() }
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

})
