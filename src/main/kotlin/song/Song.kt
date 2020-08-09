package song

import uuid.UUID
import uuid.generateUUID

data class Song(
  val id: UUID = generateUUID(),
  val title: String,
  val artist: String,
  val imageUrl: String,
  val playbackUrl: String
) {
  override fun toString(): String {
    return "Song(id=$id)"
  }
}
