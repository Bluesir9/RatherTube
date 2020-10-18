package youtube

import ui.central_content.SearchEventTrigger
import ui.central_content.SearchResultsUICoordinator
import utils.RepoResult

class SearchForYouTubeVideos {
  private val youTubeVideoRepository: YouTubeVideoRepository = YouTubeVideoRepositoryImpl
  private val searchEventTrigger: SearchEventTrigger = SearchResultsUICoordinator

  suspend operator fun invoke(query: String): RepoResult<List<YouTubeVideo>> {
    searchEventTrigger.onSearchBegin(query)
    val videosResult = youTubeVideoRepository.getVideos(query)
    searchEventTrigger.onSearchEnd(query, videosResult)
    return videosResult
  }
}
