package dev.pooq.ichor.gaia.extensions.debug

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.terminal.Terminal
import dev.pooq.ichor.gaia.extensions.env

var debug: Boolean = env("debug").toBoolean()

fun Terminal.debug(message: Any, force: Boolean = false) {
  if (!debug && !force) return
  val debugStyle = (brightBlue + bold)
  this.println(debugStyle("[DEBUG]") + white(" > ") + blue(message.toString()))
}
