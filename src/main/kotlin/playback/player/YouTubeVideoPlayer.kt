package playback.player

import com.soywiz.klock.TimeSpan
import kotlinx.coroutines.flow.Flow
import playback.queue.PlaybackQueueItem

interface YouTubeVideoPlayer {
  sealed class Event {
    sealed class WithoutPlayable : Event() {
      /*
      Will be triggered when the player has nothing to
      play. This could be the case when the app has just
      init-ed or if the user decided to clear the playback
      queue or if the user decided to individually remove
      all the items in the playback queue.
      */
      object Cleared : WithoutPlayable()
    }

    sealed class WithPlayable(open val playbackQueueItem: PlaybackQueueItem) : Event() {
      /*
      This is meant for when we are trying to load
      the streaming related data for a particular
      video.
      */
      data class Loading(override val playbackQueueItem: PlaybackQueueItem) : WithPlayable(playbackQueueItem)

      /*
      The streaming data has been fetched and we have
      forwarded it to the platform audio player so
      that it can takeover the playback task.
      */
      data class Loaded(override val playbackQueueItem: PlaybackQueueItem) : WithPlayable(playbackQueueItem)

      /*
      We failed to load the streaming data due to some API
      failure.
      */
      data class LoadFailed(
        override val playbackQueueItem: PlaybackQueueItem,
        val failureMessage: String
      ) : WithPlayable(playbackQueueItem)

      /*
      This is meant for when the platform audio player is
      trying to load further segments of the video so that
      it can continue playing it.
      */
      data class Buffering(
        override val playbackQueueItem: PlaybackQueueItem,
        val playedLength: TimeSpan
      ) : WithPlayable(playbackQueueItem)

      data class Playing(
        override val playbackQueueItem: PlaybackQueueItem,
        val playedLength: TimeSpan
      ) : WithPlayable(playbackQueueItem)

      data class Paused(
        override val playbackQueueItem: PlaybackQueueItem,
        val playedLength: TimeSpan
      ) : WithPlayable(playbackQueueItem)
    }
  }

  fun play(playbackQueueItem: PlaybackQueueItem)
  fun pause()
  fun resume()
  fun rewind()
  fun forward()
  fun clear()
  fun getCurrentlyPlayingItem(): PlaybackQueueItem?
  fun getEvents(): Flow<Event>
}
