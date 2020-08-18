package ui.central_content


sealed class CentralContentVM {
  object LoadingVM : CentralContentVM()
  object EmptyVM : CentralContentVM()
  data class ErrorVM(val message: String): CentralContentVM()
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
