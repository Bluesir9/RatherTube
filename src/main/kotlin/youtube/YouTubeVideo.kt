package youtube

import com.soywiz.klock.TimeSpan

data class YouTubeVideo(
  val id: String,
  val title: String,
  val artist: String,
  val thumbnailUrl: String,
  val length: TimeSpan
) {
  override fun toString(): String {
    return "YouTubeVideo(id=$id)"
  }
}
