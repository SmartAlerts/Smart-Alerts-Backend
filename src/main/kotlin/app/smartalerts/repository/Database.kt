package app.smartalerts.repository

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
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

data class Config(val database: DatabaseConfig)

val DatabaseModule = module {
  val databaseConfig = ConfigLoaderBuilder.default()
    .addResourceSource("/application-prod.yaml")
    .build()
    .loadConfigOrThrow<Config>()

  single { Database(databaseConfig.database) }
  single { get<SqlDelightDatabase>().userQueries }
  single { get<SqlDelightDatabase>().contactQueries }
  single { get<SqlDelightDatabase>().eventQueries }
  single { get<SqlDelightDatabase>().eventContactQueries }

}
