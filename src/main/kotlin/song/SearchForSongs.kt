package song

import ui.central_content.SearchEventTrigger
import ui.central_content.SearchResultsUICoordinator

class SearchForSongs {

  private val songsRepository: SongsRepository = SongsRepositoryImpl
  private val searchEventTrigger: SearchEventTrigger = SearchResultsUICoordinator

  suspend operator fun invoke(query: String): List<Song> {
    searchEventTrigger.onSearchBegin(query)
    val songs = songsRepository.getSongs(query)
    searchEventTrigger.onSearchEnd(query, songs)
    return songs
  }
}
