package playback.queue

import playback.player.YouTubeVideoPlayer
import playback.player.YouTubeVideoPlayerImpl

class ClearPlaybackQueue {

  private val playbackQueue: PlaybackQueue = PlaybackQueueImpl
  private val player: YouTubeVideoPlayer = YouTubeVideoPlayerImpl

  operator fun invoke() {
    playbackQueue.clear()
    player.clear()
  }
}
