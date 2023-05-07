package app.smartalerts.repository

import app.smartalerts.migrations.SmartUser
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.system.withEnvironment
import io.kotest.extensions.testcontainers.JdbcTestContainerExtension
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.shouldBe
import org.koin.dsl.koinApplication
import org.testcontainers.containers.MariaDBContainer
import javax.sql.DataSource

class DatabaseTest : FunSpec({

  val mariadb = MariaDBContainer<Nothing>("mariadb:10").apply {
    withDatabaseName("smart_alerts")
    withUsername("username")
    withPassword("password")
  }
  val dataSource = install(JdbcTestContainerExtension(mariadb))


  test("Should initialize database") {
    val host = mariadb.host
    val port = mariadb.firstMappedPort
    val user = mariadb.username
    val pass = mariadb.password

    "SmartUser" shouldNotBeIn dataSource.tables
    Database(DatabaseConfig(host, port, user, pass))
    "SmartUser" shouldBeIn dataSource.tables
  }

  test("Should let me use the database more than once") {
    val host = mariadb.host
    val port = mariadb.firstMappedPort
    val user = mariadb.username
    val pass = mariadb.password

    shouldNotThrowAny {
      repeat(10) {
        Database(DatabaseConfig(host, port, user, pass))
      }
    }
  }

  test("Should use variables from the environment to create the Database instance") {
    withEnvironment(
      mapOf(
        "config.override.database.host" to mariadb.host,
        "config.override.database.port" to mariadb.firstMappedPort.toString(),
        "DATABASE_USERNAME" to mariadb.username,
        "DATABASE_PASSWORD" to mariadb.password
      )
    ) {
      val target = koinApplication {
        modules(DatabaseModule)
      }.koin.get<Database>()

      target.userQueries.insert(SmartUser("123456"))
      target.userQueries.selectAll().executeAsOne() shouldBe "123456"
    }
  }

})

private val DataSource.tables: List<String>
  get() {
    val rs = connection.metaData.getTables(null, null, "%", null)
    val tables = mutableListOf<String>()
    while (rs.next()) {
      tables += rs.getString(3)
    }
    return tables
  }
