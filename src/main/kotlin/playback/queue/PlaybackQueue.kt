package playback.queue

import youtube.YouTubeVideo

interface PlaybackQueue {
  fun add(video: YouTubeVideo): PlaybackQueueItem
  fun remove(queueItem: PlaybackQueueItem)
  fun clear()
  fun get(): List<PlaybackQueueItem>
}
