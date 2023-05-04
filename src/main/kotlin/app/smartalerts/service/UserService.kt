package app.smartalerts.service

import app.smartalerts.migrations.SmartUser
import app.smartalerts.queries.UserQueries

class UserService(
  private val userQueries: UserQueries
) {

  fun createUser(phoneNumber: String): Result<SmartUser> {
    val user = SmartUser(phoneNumber)
    return runCatching { userQueries.insert(user) }.map { user }
  }
}

