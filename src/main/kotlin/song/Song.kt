package song

import uuid.UUID

data class Song(
  val id: UUID,
  val title: String,
  val artist: String,
  val imageUrl: String,
  val playbackUrl: String
)
