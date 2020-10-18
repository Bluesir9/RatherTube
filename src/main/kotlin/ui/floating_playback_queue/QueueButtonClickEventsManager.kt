@file:Suppress("EXPERIMENTAL_API_USAGE")

package ui.floating_playback_queue

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface QueueButtonClickEventsConsumer {
  fun onQueueButtonClicked()
}

interface QueueButtonClickEventsProvider {
  fun getClickEvents(): Flow<Unit>
}

object QueueButtonClickEventsManager : QueueButtonClickEventsConsumer, QueueButtonClickEventsProvider {

  private val clickEventsChannel = BroadcastChannel<Unit>(1)

  override fun onQueueButtonClicked() {
    clickEventsChannel.offer(Unit)
  }

  override fun getClickEvents(): Flow<Unit> = clickEventsChannel.asFlow()
}
