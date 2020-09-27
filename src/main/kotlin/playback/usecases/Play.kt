package playback.usecases

import playback.player.YouTubeVideoPlayer
import playback.player.YouTubeVideoPlayerImpl
import playback.queue.PlaybackQueue
import playback.queue.PlaybackQueueImpl
import playback.queue.PlaybackQueueItem
import youtube.YouTubeVideo

class Play {

  private val playbackQueue: PlaybackQueue = PlaybackQueueImpl
  private val player: YouTubeVideoPlayer = YouTubeVideoPlayerImpl

  operator fun invoke(video: YouTubeVideo) {
    playbackQueue.clear()
    val playbackQueueItem = playbackQueue.add(video)
    player.play(playbackQueueItem)
  }

  operator fun invoke(playbackQueueItem: PlaybackQueueItem) {
    /*
    TODO:
      The song is already present in the queue and the
      user wants to play it directly. This should not
      alter the queue in any shape and form and simply
      begin playing the song being pointed to by this
      playback queue item.
    */
  }
}