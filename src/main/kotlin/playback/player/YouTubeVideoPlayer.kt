package playback.player

import com.soywiz.klock.TimeSpan
import kotlinx.coroutines.flow.Flow
import playback.queue.PlaybackQueueItem

interface YouTubeVideoPlayer {
  sealed class Event(open val playbackQueueItem: PlaybackQueueItem) {


    /*
    This is meant for when we are trying to load
    the streaming related data for a particular
    video.
    */
    data class Loading(override val playbackQueueItem: PlaybackQueueItem): Event(playbackQueueItem)


    /*
    The streaming data has been fetched and we have
    forwarded it to the platform audio player so
    that it can takeover the playback task.
     */
    data class Loaded(override val playbackQueueItem: PlaybackQueueItem): Event(playbackQueueItem)


    /*
    We failed to load the streaming data due to some API
    failure.
    */
    data class LoadFailed(override val playbackQueueItem: PlaybackQueueItem, val failureMessage: String): Event(playbackQueueItem)


    /*
    This is meant for when the platform audio player is
    trying to load further segments of the video so that
    it can continue playing it.
    */
    data class Buffering(override val playbackQueueItem: PlaybackQueueItem, val playedLength: TimeSpan): Event(playbackQueueItem)


    data class Playing(override val playbackQueueItem: PlaybackQueueItem, val playedLength: TimeSpan): Event(playbackQueueItem)


    data class Paused(override val playbackQueueItem: PlaybackQueueItem, val playedLength: TimeSpan): Event(playbackQueueItem)
  }

  fun play(playbackQueueItem: PlaybackQueueItem)
  fun pause()
  fun resume()
  fun rewind()
  fun forward()
  fun getEvents(): Flow<Event>
}
