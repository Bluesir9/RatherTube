@file:Suppress("EXPERIMENTAL_API_USAGE")

package playback.queue

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import uuid.generateUUID
import youtube.YouTubeVideo

object PlaybackQueueImpl : PlaybackQueue {

  private val queue: MutableList<PlaybackQueueItem> by lazy { mutableListOf<PlaybackQueueItem>() }
  private val queueStreamProvider = BroadcastChannel<List<PlaybackQueueItem>>(1)

  override fun add(video: YouTubeVideo): PlaybackQueueItem {
    val playbackQueueItem = PlaybackQueueItem(id = generateUUID(), video = video)
    alterQueue { add(playbackQueueItem) }
    return playbackQueueItem
  }

  override fun remove(queueItem: PlaybackQueueItem) {
    alterQueue { remove(queueItem) }
  }

  override fun clear() {
    alterQueue { clear() }
  }

  override fun get(): List<PlaybackQueueItem> = queue

  override fun stream(): Flow<List<PlaybackQueueItem>> = queueStreamProvider.asFlow()

  private fun alterQueue(alteration: MutableList<PlaybackQueueItem>.() -> Unit) {
    queue.apply(alteration).also { queueStreamProvider.offer(it) }
  }
}
