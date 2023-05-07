package app.smartalerts

import app.smartalerts.repository.DatabaseModule
import app.smartalerts.service.ContactService
import app.smartalerts.service.EventService
import app.smartalerts.service.UserService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val KtorModule = module {
  includes(DatabaseModule)
  singleOf(::ContactService)
  singleOf(::EventService)
  singleOf(::UserService)
}
