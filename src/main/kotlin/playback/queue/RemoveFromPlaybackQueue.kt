package playback.queue

import playback.player.YouTubeVideoPlayer
import playback.player.YouTubeVideoPlayerImpl
import playback.usecases.Play

class RemoveFromPlaybackQueue {

  private val player: YouTubeVideoPlayer = YouTubeVideoPlayerImpl
  private val playbackQueue: PlaybackQueue = PlaybackQueueImpl
  private val play = Play()

  operator fun invoke(playbackQueueItem: PlaybackQueueItem) {
    val currentlyPlayingQueueItem = player.getCurrentlyPlayingItem()
    if(playbackQueueItem == currentlyPlayingQueueItem) {
      removeCurrentlyPlayingItemFromPlaybackQueue(playbackQueueItem)
    } else {
      playbackQueue.remove(playbackQueueItem)
    }
  }

  /*
  User is trying to remove the playback item that is currently being played. So go find the next
  item we should be playing and make sure that it is ready to play right after the removal happens.
  */
  private fun removeCurrentlyPlayingItemFromPlaybackQueue(currentlyPlayingItem: PlaybackQueueItem) {
    val playbackQueueItems = playbackQueue.get()
    val indexOfCurrentPlaybackItem = playbackQueueItems.indexOf(currentlyPlayingItem)
    /*
    If the currently playing item was the last item in the playback queue then there is no other item left
    to be played in the playback queue. Hence the `getOrNull` usage.
    */
    val itemToPlayNext = playbackQueueItems.getOrNull(indexOfCurrentPlaybackItem + 1)
    playbackQueue.remove(currentlyPlayingItem)
    player.clear()
    if(itemToPlayNext != null) {
      play(itemToPlayNext)
    }
  }
}
