package app.smartalerts

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.spec.IsolationMode.InstancePerTest
import io.kotest.core.test.AssertionMode.Error

@Suppress("unused")
object KotestConfiguration : AbstractProjectConfig() {
  override val isolationMode = InstancePerTest
  override val assertionMode = Error
}
