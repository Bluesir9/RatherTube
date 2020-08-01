package song


data class Song(
  /*
  FIXME:
    Ideally this should have been a UUID object
    but since I am unable to use the uuid JS npm
    library via Kotlin, this value will be set as
    the YouTube video id.
    I should however try to figure out how to invoke
    uuid JS code from Kotlin codebase.
   */
  val id: String,
  val title: String,
  val artist: String,
  val imageUrl: String,
  val playbackUrl: String
)
