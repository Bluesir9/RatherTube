package youtube

import ui.central_content.SearchEventTrigger
import ui.central_content.SearchResultsUICoordinator

class SearchForYouTubeVideos {
  private val youTubeVideoRepository: YouTubeVideoRepository = YouTubeVideoRepositoryImpl
  private val searchEventTrigger: SearchEventTrigger = SearchResultsUICoordinator

  suspend operator fun invoke(query: String): List<YouTubeVideo> {
    searchEventTrigger.onSearchBegin(query)
    val videos = youTubeVideoRepository.getVideos(query)
    searchEventTrigger.onSearchEnd(query, videos)
    return videos
  }
}
