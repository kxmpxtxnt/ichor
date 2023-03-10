package dev.pooq.ichor.gaia.networking.packet.server.play

import dev.pooq.ichor.gaia.extensions.bytes.buffer
import dev.pooq.ichor.gaia.extensions.bytes.uncompressedBuffer
import dev.pooq.ichor.gaia.extensions.bytes.varInt
import dev.pooq.ichor.gaia.networking.INT
import dev.pooq.ichor.gaia.networking.ServerPacket
import dev.pooq.ichor.gaia.networking.VAR_INT
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

class SetCompressionPacket(
  val threshold: Int
) : ServerPacket() {

  override suspend fun serialize(): ByteBuffer {
    return uncompressedBuffer(INT + VAR_INT) {
      putInt(id)
      varInt(threshold)
    }
  }

  override val id: Int
    get() = 0x03

  override val state: State
    get() = State.PLAY
}