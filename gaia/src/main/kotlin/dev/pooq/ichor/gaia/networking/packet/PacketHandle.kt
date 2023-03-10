package dev.pooq.ichor.gaia.networking.packet

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import dev.pooq.ichor.gaia.extensions.bytes.compress
import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.extensions.terminal
import dev.pooq.ichor.gaia.networking.ServerPacket
import io.ktor.network.sockets.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import kotlin.coroutines.CoroutineContext

class PacketHandle(
  var state: State,
  val connection: Connection,
  var threshold: Int = -1,
  var compression: Boolean = threshold > 0,
  val coroutineContext: CoroutineContext
) {

  suspend fun sendPacket(packet: ServerPacket) {
    withContext(coroutineContext) {
      launch {
        connection.output.writeAvailable(packet.serialize().run {
          if (compression && limit() >= threshold) ByteBuffer.wrap(array().compress()) else this
        })
        terminal.debug(
          TextStyles.bold(
            """
            ${TextColors.brightMagenta("--- Sent packet ---")}
                      ${TextColors.brightGreen("Packet: ${packet.id}")}
                      ${
              TextColors.brightYellow(
                "Name: ${packet.javaClass.simpleName}"
              )
            }
                      ${TextColors.brightMagenta("-----------------------")}
          """.trimIndent()
          )
        )
      }
    }
  }
}
