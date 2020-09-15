package playback.usecases

import playback.player.YouTubeVideoPlayer
import playback.player.YouTubeVideoPlayerImpl
import playback.queue.PlaybackQueue
import playback.queue.PlaybackQueueImpl
import youtube.YouTubeVideo

class Play {

  private val playbackQueue: PlaybackQueue = PlaybackQueueImpl
  private val player: YouTubeVideoPlayer = YouTubeVideoPlayerImpl

  operator fun invoke(video: YouTubeVideo) {
    playbackQueue.clear()
    val playbackQueueItem = playbackQueue.add(video)
    player.play(playbackQueueItem)
  }
}
