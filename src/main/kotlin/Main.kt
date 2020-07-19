import logging.LoggerImpl
import ui.body.Body
import kotlin.browser.window

private val logger = LoggerImpl("Main.kt")
private lateinit var body: Body

fun main() {
  logger.debug("RatherTube begins")
  window.addEventListener("load", {
    body = Body()
    body.create()
  })
  window.addEventListener("unload", {
    body.destroy()
  })
}
