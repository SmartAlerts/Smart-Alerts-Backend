@file:OptIn(ExperimentalSerializationApi::class)

package app.smartalerts.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime

@Serializer(LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
  override val descriptor = PrimitiveSerialDescriptor("LocalDateTime", STRING)

  override fun deserialize(decoder: Decoder): LocalDateTime = decoder.decodeString().let { LocalDateTime.parse(it) }

  override fun serialize(encoder: Encoder, value: LocalDateTime) = encoder.encodeString(value.toString())

}
