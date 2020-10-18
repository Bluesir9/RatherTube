package playback.usecases

import playback.player.YouTubeVideoPlayer
import playback.player.YouTubeVideoPlayerImpl
import playback.queue.ClearPlaybackQueue
import playback.queue.PlaybackQueue
import playback.queue.PlaybackQueueImpl
import playback.queue.PlaybackQueueItem
import youtube.YouTubeVideo

class Play {

  private val clearPlaybackQueue = ClearPlaybackQueue()
  private val playbackQueue: PlaybackQueue = PlaybackQueueImpl
  private val player: YouTubeVideoPlayer = YouTubeVideoPlayerImpl

  operator fun invoke(video: YouTubeVideo) {
    clearPlaybackQueue()
    val playbackQueueItem = playbackQueue.add(video)
    player.play(playbackQueueItem)
  }

  operator fun invoke(playbackQueueItem: PlaybackQueueItem) {
    player.play(playbackQueueItem)
  }
}
