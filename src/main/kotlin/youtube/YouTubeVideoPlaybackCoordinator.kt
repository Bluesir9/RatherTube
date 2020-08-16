@file:Suppress("EXPERIMENTAL_API_USAGE")

package youtube

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.w3c.dom.Audio
import youtube.YouTubeVideoPlaybackEventListener.Event

interface YouTubeVideoPlayer {
  suspend fun playRequested(video: YouTubeVideo)
  fun pauseRequested()
  fun rewindRequested()
  fun forwardRequested()
}

interface YouTubeVideoPlaybackEventListener {
  sealed class Event(open val video: YouTubeVideo) {
    /*
    This is meant for when we are trying to load
    the streaming related data for a particular
    video.
     */
    data class Loading(override val video: YouTubeVideo): Event(video)
    /*
    The streaming data has been fetched and we have
    forwarded it to the platform audio player so
    that it can takeover the playback task.
     */
    data class Loaded(override val video: YouTubeVideo): Event(video)
    /*
    This is meant for when the platform audio player is
    trying to load further segments of the video so that
    it can continue playing it.
     */
    data class Buffering(override val video: YouTubeVideo): Event(video)
    data class Playing(override val video: YouTubeVideo): Event(video)
    data class Paused(override val video: YouTubeVideo): Event(video)
  }
  fun getSongPlaybackEvents(): Flow<Event>
}

object YouTubeVideoPlaybackCoordinator : YouTubeVideoPlayer, YouTubeVideoPlaybackEventListener {

  private data class MediaFile(val video: YouTubeVideo, val platformAudio: Audio)

  private val youTubeVideoRepository: YouTubeVideoRepository = YouTubeVideoRepositoryImpl
  private val eventChannel = BroadcastChannel<Event>(1)
  private var latestMediaFile : MediaFile? = null

  override suspend  fun playRequested(video: YouTubeVideo) {
    val latestMediaFileCopy = latestMediaFile
    if(latestMediaFileCopy != null && latestMediaFileCopy.video == video) {
      latestMediaFileCopy.platformAudio.play()
    } else {
      broadcastEvent(Event.Loading(video))
      val audioPlaybackUrl = youTubeVideoRepository.getStreamUrl(video)
      broadcastEvent(Event.Loaded(video))
      val newAudio = Audio(audioPlaybackUrl)
      val newMediaFile = MediaFile(video, newAudio)
      onNewMediaFileObtained(newMediaFile)
      latestMediaFileCopy?.platformAudio?.src = ""
      newMediaFile.platformAudio.play()
    }
  }

  private fun onNewMediaFileObtained(mediaFile: MediaFile) {
    mediaFile.platformAudio.onplay = {
      broadcastEvent(Event.Playing(mediaFile.video))
    }
    mediaFile.platformAudio.onplaying = {
      broadcastEvent(Event.Playing(mediaFile.video))
    }
    mediaFile.platformAudio.onpause = {
      broadcastEvent(Event.Paused(mediaFile.video))
    }
    mediaFile.platformAudio.onprogress = {
      /*
      FIXME:
       Ensure that this is the best API
       to track this event.
       */
      broadcastEvent(Event.Loading(mediaFile.video))
    }
    this.latestMediaFile = mediaFile
  }

  private fun broadcastEvent(event: Event) {
    eventChannel.offer(event)
  }

  override fun pauseRequested() {
    latestMediaFile?.platformAudio?.pause()
  }

  override fun rewindRequested() {
    latestMediaFile?.platformAudio?.also {
      it.pause()
      it.fastSeek(0.0)
    }
  }

  override fun forwardRequested() {
    /*
    TODO:
      Playback queue feature needs to be
      implemented.
     */
  }

  override fun getSongPlaybackEvents(): Flow<Event> =
    eventChannel.asFlow()
}
