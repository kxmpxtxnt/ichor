package dev.pooq.ichor.hephaistos

import com.github.ajalt.mordant.rendering.TextColors.brightGreen
import com.github.ajalt.mordant.rendering.TextColors.red
import dev.pooq.ichor.gaia.extensions.debug.debug
import dev.pooq.ichor.gaia.extensions.log
import dev.pooq.ichor.gaia.networking.packet.ClientPackets
import dev.pooq.ichor.gaia.server.Server
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Hephaistos : Server() {

  override suspend fun startup(args: Array<String>) {
    terminal.log(brightGreen("Starting server..."))

    args.forEach { terminal.debug(it) }

    val manager = SelectorManager(Dispatchers.Default)
    val serverSocket = aSocket(manager).tcp().bind(InetSocketAddress("127.0.0.1", 25565))

    terminal.log(brightGreen("Server started successfully!"))

    while (!serverSocket.isClosed) {
      val socket = serverSocket.accept()

      val connection = Connection(socket, socket.openReadChannel(), socket.openWriteChannel(true))

      val client = socket.handle(connection)

      connection.input.read(min = 10) { buffer ->
        launch {
          do {
            ClientPackets.deserializeAndHandle(buffer, client, this@Hephaistos)
          } while (!connection.input.isClosedForRead)
        }
      }
    }
  }

  override suspend fun shutdown() {
    terminal.log(red("Shutdown"))
  }
}

suspend fun main(args: Array<String>) {
  Hephaistos.startup(args)
}