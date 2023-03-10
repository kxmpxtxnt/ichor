package dev.pooq.ichor.gaia.networking.packet.client.handshaking

import dev.pooq.ichor.gaia.extensions.bytes.short
import dev.pooq.ichor.gaia.extensions.bytes.string
import dev.pooq.ichor.gaia.extensions.bytes.varInt
import dev.pooq.ichor.gaia.networking.ClientPacket
import dev.pooq.ichor.gaia.networking.packet.State
import dev.pooq.ichor.gaia.networking.packet.receive.receivers.handshaking.HandshakeReceiver
import java.nio.ByteBuffer

data class Handshake(
  val protocolVersion: Int,
  val serverAddress: String,
  val serverPort: Short,
  val nextState: State
) : ClientPacket() {

  override val id: Int
    get() = 0x00

  override val state: State
    get() = State.HANDSHAKING

  companion object : PacketProcessor<Handshake>(HandshakeReceiver()) {
    override suspend fun deserialize(byteBuffer: ByteBuffer): Handshake {
      return Handshake(
        byteBuffer.varInt(),
        byteBuffer.string(),
        byteBuffer.short(),
        when (byteBuffer.varInt()) {
          1 -> State.STATUS
          2 -> State.LOGIN

          else -> throw IllegalArgumentException("StateId must be 1 or 2.")
        }
      )
    }
  }
}