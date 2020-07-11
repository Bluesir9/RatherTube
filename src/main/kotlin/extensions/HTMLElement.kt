@file:Suppress("EXPERIMENTAL_API_USAGE")

package extensions

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.w3c.dom.HTMLElement

fun HTMLElement.clickEventsAsFlow(): Flow<Unit> {
  val channel = BroadcastChannel<Unit>(1)
  this.onclick = { channel.offer(Unit) }
  return channel.asFlow()
}
