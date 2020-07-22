package ui.central_content

import song.Song
import ui.central_content.CentralContentVM.SearchResults
import ui.central_content.SearchEvent.Loaded
import ui.central_content.SearchEvent.Loading
import ui.central_content.CentralContentVM.Loading as LoadingVM
import ui.central_content.CentralContentVM.Empty as EmptyVM

interface CentralContentVMGenerator {
  operator fun invoke(searchEvent: SearchEvent): CentralContentVM
}

class CentralContentVMGeneratorImpl : CentralContentVMGenerator {
  override fun invoke(searchEvent: SearchEvent): CentralContentVM =
    when (searchEvent) {
      is Loading -> LoadingVM
      is Loaded -> {
        if (searchEvent.songs.isEmpty()) EmptyVM
        else getSearchResultsVM(searchEvent.songs)
      }
    }

  private fun getSearchResultsVM(songs: List<Song>): SearchResults =
    songs
      .map(this::getSearchResultItem)
      .let { items -> SearchResults(items) }

  private fun getSearchResultItem(song: Song): SearchResults.Item =
    SearchResults.Item(
      id = song.id,
      trackTitle = song.title,
      trackArtist = song.artist,
      trackImageUrl = song.imageUrl
    )
}
