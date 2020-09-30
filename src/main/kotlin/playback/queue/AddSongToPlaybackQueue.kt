package playback.queue

import playback.player.YouTubeVideoPlayer
import playback.player.YouTubeVideoPlayerImpl
import youtube.YouTubeVideo

class AddSongToPlaybackQueue {

  private val playbackQueue: PlaybackQueue = PlaybackQueueImpl
  private val player: YouTubeVideoPlayer = YouTubeVideoPlayerImpl

  operator fun invoke(video: YouTubeVideo) {
    val playbackQueueItems = playbackQueue.get()
    if(playbackQueueItems.isEmpty()) {
      /*
      Since the playback queue is currently empty,
      we will add the song to the playback queue
      and immediately start playing it.
      */
      val playbackQueueItem = playbackQueue.add(video)
      player.play(playbackQueueItem)
    } else {
      playbackQueue.add(video)
    }
  }
}
