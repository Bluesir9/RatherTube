package playback.queue

import uuid.generateUUID
import youtube.YouTubeVideo

object PlaybackQueueImpl : PlaybackQueue {

  private val queue: MutableList<PlaybackQueueItem> by lazy { mutableListOf<PlaybackQueueItem>() }

  override fun add(video: YouTubeVideo): PlaybackQueueItem {
    val playbackQueueItem = PlaybackQueueItem(id = generateUUID(), video = video)
    queue.add(playbackQueueItem)
    return playbackQueueItem
  }

  override fun remove(queueItem: PlaybackQueueItem) {
    queue.remove(queueItem)
  }

  override fun clear() {
    queue.clear()
  }

  override fun get(): List<PlaybackQueueItem> = queue
}
