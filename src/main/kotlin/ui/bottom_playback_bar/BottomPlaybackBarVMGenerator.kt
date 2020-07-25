package ui.bottom_playback_bar

import song.Song
import song.SongPlaybackEventListener.Event
import song.SongPlaybackEventListener.Event.*

interface BottomPlaybackBarVMGenerator {
  operator fun invoke(playbackEvent: Event): BottomPlaybackBarVM
}

class BottomPlaybackBarVMGeneratorImpl : BottomPlaybackBarVMGenerator {

  override fun invoke(playbackEvent: Event): BottomPlaybackBarVM =
    when (playbackEvent) {
      //FIXME: Create dedicated representation for song loading state
      is Loading,
      is Playing -> getVM(song = playbackEvent.song, playButtonVisible = false, pauseButtonVisible = true)
      is Paused -> getVM(song = playbackEvent.song, playButtonVisible = true, pauseButtonVisible = false)
    }

  private fun getVM(song: Song, playButtonVisible: Boolean, pauseButtonVisible: Boolean): BottomPlaybackBarVM =
    BottomPlaybackBarVM(
      trackTitle = song.title,
      trackArtist = song.artist,
      playButtonVisible = playButtonVisible,
      pauseButtonVisible = pauseButtonVisible
    )
}
