package app.smartalerts.service

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import app.smartalerts.Database
import app.smartalerts.migrations.Contact
import br.com.colman.kotest.extensions.H2Listener
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe

class ContactServiceTest : FunSpec({
  val h2Listener = listener(H2Listener("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"))
  val driver = h2Listener.dataSource.asJdbcDriver()

  val db = Database(driver)
  beforeTest { Database.Schema.create(driver) }

  val userService = UserService(db.userQueries)
  val target = ContactService(db.contactQueries)

  val smartUser by lazy { userService.createUser("123456").getOrThrow() }

  test("Create contact") {
    target.addContact(smartUser, "654321").shouldBeSuccess()
    db.contactQueries.findBySmartUser(smartUser.phone).executeAsOne() shouldBe Contact(smartUser.phone, "654321")
  }

  test("Should not insert duplicate contacts") {
    target.addContact(smartUser, "654321").shouldBeSuccess()
    target.addContact(smartUser, "654321").shouldBeFailure()
  }

  test("Should allow inserting more than one contact") {
    target.addContact(smartUser, "654321")
    target.addContact(smartUser, "098")

    db.contactQueries.findBySmartUser(smartUser.phone).executeAsList() shouldContainExactlyInAnyOrder  listOf(
      Contact(smartUser.phone, "654321"), Contact(smartUser.phone, "098")
    )
  }

})
