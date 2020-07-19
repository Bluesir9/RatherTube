package ui.bottom_playback_bar

data class BottomPlaybackBarVM(
  val trackTitle: String,
  val trackArtist: String,
  val playButtonVisible: Boolean,
  val pauseButtonVisible: Boolean
)
