package ui.bottom_playback_bar

data class BottomPlaybackBarVM(
  val trackTitle: String,
  val trackArtist: String,
  val playButtonVisible: Boolean,
  val pauseButtonVisible: Boolean,
  val progressBarVM: ProgressBarVM
) {
  sealed class ProgressBarVM {
    object Hidden : ProgressBarVM()
    data class Visible(
      val progressBarWidthPercent: Double,
      val progressDotLeftMarginPercent: Double,
      /*
      String will have the following format ->
      ${minutes_breakup_of_length_played}:${seconds_breakup_of_length_played} / ${minutes_breakup_of_total_length}:${seconds_breakup_of_total_length}

      So, if a song that is 140 seconds long has had 75 seconds worth of playback, then we will show the following ->
      01:15 / 02:20
      */
      val progressTimestamp: String
    ): ProgressBarVM()
  }
}
