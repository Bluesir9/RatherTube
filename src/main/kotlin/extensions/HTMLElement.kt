@file:Suppress("EXPERIMENTAL_API_USAGE")

package extensions

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

fun HTMLElement.clickEventsAsFlow(): Flow<Unit> {
  val channel = BroadcastChannel<Unit>(1)
  this.onclick = { channel.offer(Unit) }
  return channel.asFlow()
}

fun HTMLElement.getFirstHTMLElementByClassName(className: String): HTMLElement =
  this.getElementsByClassName(className)[0] as HTMLElement

enum class StyleDisplay(val value: String) {
  Flex("flex")
}

fun HTMLElement.show(styleDisplay: StyleDisplay) {
  this.style.display = styleDisplay.value
}

fun HTMLElement.hide() {
  this.style.display = "none"
}
