package app.smartalerts.repository

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import app.smartalerts.Config
import org.koin.dsl.module
import org.mariadb.jdbc.MariaDbPoolDataSource
import app.smartalerts.Database as SqlDelightDatabase

class Database(
  private val config: DatabaseConfig
) : SqlDelightDatabase by SqlDelightDatabase(config.createJdbcDriver()) {
  init {
    runCatching { SqlDelightDatabase.Schema.create(config.createJdbcDriver()) }
  }
}

data class DatabaseConfig(val host: String, val port: Int, val username: String, val password: String) {

  private val jdbcUrl = "jdbc:mariadb://$host:$port/smart_alerts?user=$username&password=$password"

  fun createJdbcDriver() = createDataSource().asJdbcDriver()
  private fun createDataSource() = MariaDbPoolDataSource(jdbcUrl)
}

val DatabaseModule = module {
  single { Database(get<Config>().database) }
  single { get<SqlDelightDatabase>().userQueries }
  single { get<SqlDelightDatabase>().contactQueries }
  single { get<SqlDelightDatabase>().eventQueries }
  single { get<SqlDelightDatabase>().eventContactQueries }
}
