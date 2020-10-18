import config.ENVIRONMENT
import org.w3c.dom.HTMLScriptElement
import ui.body.Body
import kotlin.browser.document
import kotlin.browser.window

private lateinit var body: Body

fun main() {
  window.addEventListener("load", {
    addFontAwesomeScriptElement()
    body = Body()
    body.create()
  })
  window.addEventListener("unload", {
    body.destroy()
  })
}

private fun addFontAwesomeScriptElement() {
  val script = document.createElement("script") as HTMLScriptElement
  script.src = ENVIRONMENT.fontAwesomeScriptUrl
  script.crossOrigin = "anonymous"
  document.head!!.appendChild(script)
}