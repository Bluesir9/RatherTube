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
    val itemToPlayNext = getItemToPlayNext(playbackQueueItems, indexOfCurrentPlaybackItem)
    playbackQueue.remove(currentlyPlayingItem)
    player.clear()
    if(itemToPlayNext != null) {
      play(itemToPlayNext)
    }
  }

  @Suppress("LiftReturnOrAssignment")
  private fun getItemToPlayNext(
    playbackQueueItems: List<PlaybackQueueItem>,
    currentPlaybackItemIndex: Int
  ): PlaybackQueueItem? {
    /*
    Start by trying to find the item right after the current playback item.
    */
    val itemAfterCurrentPlaybackItem = playbackQueueItems.getOrNull(currentPlaybackItemIndex + 1)
    if(itemAfterCurrentPlaybackItem != null) {
      return itemAfterCurrentPlaybackItem
    } else {
      /*
      Current item was the last item in the playback queue. Lets find the previous item.
      */
      val itemBeforeCurrentPlaybackItem = playbackQueueItems.getOrNull(currentPlaybackItemIndex - 1)
      if(itemBeforeCurrentPlaybackItem != null) {
        return itemBeforeCurrentPlaybackItem
      } else {
        /*
        Current item was the singular item in the playback queue. Removing it will make
        the queue empty. Nothing to play after this ¯\_(ツ)_/¯
        */
        return null
      }
    }
  }
}
