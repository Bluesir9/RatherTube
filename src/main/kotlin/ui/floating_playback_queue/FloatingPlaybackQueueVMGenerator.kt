@file:Suppress("MoveLambdaOutsideParentheses")

package ui.floating_playback_queue

import config.Color
import playback.queue.PlaybackQueueItem

/*
FIXME:
  If this will have exactly 1 implementation,
  then why have an interface at all, just use
  the class directly.  
 */
interface FloatingPlaybackQueueVMGenerator {
  /*
  `queue` can be an empty list.
   */
  operator fun invoke(
    currentVM: FloatingPlaybackQueueVM,
    queue: List<PlaybackQueueItem>,
    currentPlaybackItem: PlaybackQueueItem?
  ): FloatingPlaybackQueueVM
}

class FloatingPlaybackQueueVMGeneratorImpl : FloatingPlaybackQueueVMGenerator {

  override fun invoke(
    currentVM: FloatingPlaybackQueueVM,
    queue: List<PlaybackQueueItem>,
    currentPlaybackItem: PlaybackQueueItem?
  ): FloatingPlaybackQueueVM =
    FloatingPlaybackQueueVM(
      visible = currentVM.visible,
      clearButtonEnabled = queue.isNotEmpty(),
      items = queue.map { queueItem -> getItemVMs(queueItem, queueItem == currentPlaybackItem) }
    )

  private fun getItemVMs(queueItem: PlaybackQueueItem, isCurrentlyPlaying: Boolean): FloatingPlaybackQueueVM.Item =
    FloatingPlaybackQueueVM.Item(
      id = queueItem.id,
      backgroundColor = getBackgroundColor(isCurrentlyPlaying),
      trackTitle = queueItem.video.title,
      trackArtist = queueItem.video.artist,
      isActivelyPlaying = isCurrentlyPlaying
    )

  private fun getBackgroundColor(isItemCurrentlyPlaying: Boolean): String =
    if(isItemCurrentlyPlaying) Color.BACKGROUND_PLAYBACK_QUEUE_ITEM_ACTIVELY_PLAYING
    else Color.BACKGROUND_BLACK
}
