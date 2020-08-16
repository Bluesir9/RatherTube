package youtube

data class YouTubeVideo(
  val id: String,
  val title: String,
  val artist: String,
  val thumbnailUrl: String
) {
  override fun toString(): String {
    return "YouTubeVideo(id=$id)"
  }
}
