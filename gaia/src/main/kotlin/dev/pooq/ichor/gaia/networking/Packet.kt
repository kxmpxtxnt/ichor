package dev.pooq.ichor.gaia.networking

import dev.pooq.ichor.gaia.networking.handle.PacketHandle
import dev.pooq.ichor.gaia.networking.packet.PacketHandler
import dev.pooq.ichor.gaia.networking.packet.State
import java.nio.ByteBuffer

interface Packet {
  val id: Int
  val state: State
}

abstract class ClientPacket : Packet{

  abstract class PacketDeserializer<P: ClientPacket>(private vararg val handlers: PacketHandler<P>) {
    protected abstract suspend fun deserialize(byteBuffer: ByteBuffer): P

    suspend fun deserializeAndHandle(byteBuffer: ByteBuffer, packetHandle: PacketHandle): P {
      val packet = deserialize(byteBuffer)

      handlers.forEach {
        it.onReceive(packet, packetHandle)
      }

      return packet
    }
  }
}

abstract class ServerPacket : Packet{
  abstract suspend fun serialize(): ByteBuffer
}
