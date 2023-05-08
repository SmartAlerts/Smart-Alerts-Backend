package app.smartalerts

import app.smartalerts.repository.DatabaseConfig
import app.smartalerts.repository.DatabaseModule
import app.smartalerts.service.ContactService
import app.smartalerts.service.EventService
import app.smartalerts.service.UserService
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val configuration = ConfigLoaderBuilder.default().addResourceSource("/application-prod.yaml").build()
data class Config(val database: DatabaseConfig)

val ConfigurationModule = module {
  single { configuration.loadConfigOrThrow<Config>() }
}

val KtorModule = module {
  includes(DatabaseModule)
  includes(ConfigurationModule)

  singleOf(::ContactService)
  singleOf(::EventService)
  singleOf(::UserService)
}
