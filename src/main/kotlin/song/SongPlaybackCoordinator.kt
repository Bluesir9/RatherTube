@file:Suppress("EXPERIMENTAL_API_USAGE")

package song

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import song.SongPlaybackEventListener.Event

interface SongPlayer {
  fun playRequested(song: Song)
  fun pauseRequested()
  fun rewindRequested()
  fun forwardRequested()
}

interface SongPlaybackEventListener {
  sealed class Event(open val song: Song) {
    data class Loading(override val song: Song): Event(song)
    data class Playing(override val song: Song): Event(song)
    data class Paused(override val song: Song): Event(song)
  }
  fun getSongPlaybackEvents(): Flow<Event>
}

object SongPlaybackCoordinator : SongPlayer, SongPlaybackEventListener {

  private val eventChannel = BroadcastChannel<Event>(1)

  override fun playRequested(song: Song) {
    TODO("Not yet implemented")
  }

  override fun pauseRequested() {
    TODO("Not yet implemented")
  }

  override fun rewindRequested() {
    TODO("Not yet implemented")
  }

  override fun forwardRequested() {
    TODO("Not yet implemented")
  }

  override fun getSongPlaybackEvents(): Flow<Event> =
    eventChannel.asFlow()
}
