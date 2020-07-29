@file:Suppress("EXPERIMENTAL_API_USAGE")

package song

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.w3c.dom.Audio
import song.SongPlaybackEventListener.Event
import song.SongPlaybackEventListener.Event.*

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

  private data class AudioFile(val song: Song, val audio: Audio)

  private val eventChannel = BroadcastChannel<Event>(1)
  private var latestAudioFile : AudioFile? = null

  override fun playRequested(song: Song) {
    val latestAudioFileCopy = latestAudioFile
    if(latestAudioFileCopy != null && latestAudioFileCopy.song == song) {
      latestAudioFileCopy.audio.play()
    } else {
      val newAudio = Audio(song.playbackUrl)
      val newAudioFile = AudioFile(song, newAudio)
      onNewAudioFileObtained(newAudioFile)
      newAudioFile.audio.play()
    }
  }

  private fun onNewAudioFileObtained(audioFile: AudioFile) {
    audioFile.audio.onplay = {
      broadcastEvent(Playing(audioFile.song))
    }
    audioFile.audio.onplaying = {
      broadcastEvent(Playing(audioFile.song))
    }
    audioFile.audio.onpause = {
      broadcastEvent(Paused(audioFile.song))
    }
    audioFile.audio.onprogress = {
      /*
      FIXME:
       Ensure that this is the best API
       to track this event.
       */
      broadcastEvent(Loading(audioFile.song))
    }
    this.latestAudioFile = audioFile
  }

  private fun broadcastEvent(event: Event) {
    eventChannel.offer(event)
  }

  override fun pauseRequested() {
    latestAudioFile?.audio?.pause()
  }

  override fun rewindRequested() {
    latestAudioFile?.audio?.also {
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
