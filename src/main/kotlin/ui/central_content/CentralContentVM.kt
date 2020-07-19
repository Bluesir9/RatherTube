package ui.central_content

import uuid.UUID
import uuid.randomUUID

sealed class CentralContentVM {
  object Loading : CentralContentVM()
  object Empty : CentralContentVM()
  data class SearchResults(val items: List<Item>): CentralContentVM() {
    data class Item(
      val id: UUID = randomUUID(),
      val trackTitle: String,
      val trackArtist: String,
      val trackImageUrl: String
    )
  }
}
