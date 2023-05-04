package app.smartalerts.service

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import app.smartalerts.Database
import br.com.colman.kotest.extensions.H2Listener
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe

class UserServiceTest : FunSpec({
  val h2Listener = listener(H2Listener("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"))
  val driver = h2Listener.dataSource.asJdbcDriver()

  val db = Database(driver)
  beforeTest { Database.Schema.create(driver) }

  val target = UserService(db.userQueries)

  test("Insert") {
    target.createUser("123456").shouldBeSuccess()

    db.userQueries.selectAll().executeAsList().shouldBeSingleton {
      it shouldBe "123456"
    }
  }

  test("Should not insert duplicates") {
    target.createUser("123456").shouldBeSuccess()
    target.createUser("123456").shouldBeFailure()
  }
})
