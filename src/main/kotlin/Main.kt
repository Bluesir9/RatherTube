import ui.body.Body
import kotlin.browser.window

private lateinit var body: Body

fun main() {
  window.addEventListener("load", {
    body = Body()
    body.create()
  })
  window.addEventListener("unload", {
    body.destroy()
  })
}