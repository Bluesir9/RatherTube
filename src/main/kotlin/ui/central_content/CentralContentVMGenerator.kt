package ui.central_content

import ui.central_content.CentralContentVM.SearchResults
import ui.central_content.SearchEvent.*
import youtube.YouTubeVideo
import ui.central_content.CentralContentVM.Loading as LoadingVM
import ui.central_content.CentralContentVM.Empty as EmptyVM
import ui.central_content.CentralContentVM.Error as ErrorVM

interface CentralContentVMGenerator {
  operator fun invoke(searchEvent: SearchEvent): CentralContentVM
}

class CentralContentVMGeneratorImpl : CentralContentVMGenerator {
  override fun invoke(searchEvent: SearchEvent): CentralContentVM =
    when (searchEvent) {
      is Loading -> LoadingVM
      is Loaded -> {
        if (searchEvent.videos.isEmpty()) EmptyVM
        else getSearchResultsVM(searchEvent.videos)
      }
      is LoadFailed -> ErrorVM(searchEvent.failureMessage)
    }

  private fun getSearchResultsVM(videos: List<YouTubeVideo>): SearchResults =
    videos
      .map(this::getSearchResultItem)
      .let { items -> SearchResults(items) }

  private fun getSearchResultItem(video: YouTubeVideo): SearchResults.Item =
    SearchResults.Item(
      id = video.id,
      trackTitle = video.title,
      trackArtist = video.artist,
      trackImageUrl = video.thumbnailUrl
    )
}
