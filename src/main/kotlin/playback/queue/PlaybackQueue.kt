package playback.queue

import kotlinx.coroutines.flow.Flow
import youtube.YouTubeVideo

interface PlaybackQueue {
  fun add(video: YouTubeVideo): PlaybackQueueItem
  fun remove(queueItem: PlaybackQueueItem)
  fun clear()
  fun get(): List<PlaybackQueueItem>
  fun stream(): Flow<List<PlaybackQueueItem>>
}
