package ui.floating_playback_queue

import uuid.UUID

data class FloatingPlaybackQueueVM(
  val visible: Boolean = false,
  val clearButtonEnabled: Boolean = false,
  /*
  Can be empty
  */
  val items: List<Item> = emptyList()
) {

  data class Item(
    /*
    This is meant to point to the actual
    playback queue item that we wish to
    render.
    */
    val id: UUID,
    /*
    The song that is currently being
    played will have a lighter background
    color than other songs in the playback
    queue.
    */
    val backgroundColor: String,
    val trackTitle: String,
    val trackArtist: String,
    val isActivelyPlaying: Boolean
  )
}
