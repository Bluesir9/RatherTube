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
  operator fun invoke(currentVM: FloatingPlaybackQueueVM, queue: List<PlaybackQueueItem>): FloatingPlaybackQueueVM
}

class FloatingPlaybackQueueVMGeneratorImpl : FloatingPlaybackQueueVMGenerator {

  override fun invoke(currentVM: FloatingPlaybackQueueVM, queue: List<PlaybackQueueItem>): FloatingPlaybackQueueVM =
    FloatingPlaybackQueueVM(
      visible = currentVM.visible,
      clearButtonEnabled = queue.isNotEmpty(),
      items = queue.mapIndexed { index, queueItem -> getItemVMs(index, queueItem) }
    )

  private fun getItemVMs(index: Int, queueItem: PlaybackQueueItem): FloatingPlaybackQueueVM.Item =
    FloatingPlaybackQueueVM.Item(
      id = queueItem.id,
      backgroundColor = getBackgroundColor(index),
      trackTitle = queueItem.video.title,
      trackArtist = queueItem.video.artist,
      thumbnailUrl = queueItem.video.thumbnailUrl
    )

  /*
  FIXME:
    The actual player will need to update
    the playback queue to indicate which item
    is actively being played so that we can
    implement this function more accurately
    to indicate the queue item which is being
    played actively.
   */
  private fun getBackgroundColor(index: Int): String =
    if(index == 0) Color.BACKGROUND_PLAYBACK_QUEUE_ITEM_ACTIVELY_PLAYING
    else Color.BACKGROUND_PLAYBACK_QUEUE_ITEM_NOT_ACTIVELY_PLAYING
}
