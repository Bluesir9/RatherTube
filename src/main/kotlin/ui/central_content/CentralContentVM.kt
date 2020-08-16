package ui.central_content


sealed class CentralContentVM {
  object Loading : CentralContentVM()
  object Empty : CentralContentVM()
  data class SearchResults(val items: List<Item>): CentralContentVM() {
    data class Item(
      val id: String,
      val trackTitle: String,
      val trackArtist: String,
      val trackImageUrl: String
    ) {
      override fun toString(): String {
        return "Item(id=$id)"
      }
    }
  }
}
