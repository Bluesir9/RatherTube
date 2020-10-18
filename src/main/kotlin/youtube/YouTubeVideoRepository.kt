package youtube

import ktor.toRepoResult
import utils.RepoResult
import youtube.api.get_video_stream.GetYouTubeVideoStreamApi
import youtube.api.get_video_stream.GetYouTubeVideoStreamApiImpl
import youtube.api.search.SearchYouTubeVideosApi
import youtube.api.search.SearchYouTubeVideosApiImpl
import youtube.api.search.SearchYouTubeVideosApiResponse

interface YouTubeVideoRepository {
  /*
  FIXME: Not sure if I like this RepoResult pattern.
  */
  suspend fun getVideos(searchQuery: String): RepoResult<List<YouTubeVideo>>
  suspend fun getStreamUrl(video: YouTubeVideo): RepoResult<String>
}

object YouTubeVideoRepositoryImpl : YouTubeVideoRepository {
  private val searchYouTubeVideosApi: SearchYouTubeVideosApi = SearchYouTubeVideosApiImpl()
  private val getYouTubeVideoStreamApi: GetYouTubeVideoStreamApi = GetYouTubeVideoStreamApiImpl()

  override suspend fun getVideos(searchQuery: String): RepoResult<List<YouTubeVideo>> =
    searchYouTubeVideosApi.search(searchQuery)
      .let { apiResult -> apiResult.toRepoResult { getYouTubeVideos(it) } }

  private fun getYouTubeVideos(response: SearchYouTubeVideosApiResponse): List<YouTubeVideo> =
    response.items
      .map { item ->
        YouTubeVideo(
          id = item.videoId,
          title = item.videoTitle,
          artist = item.videoArtist,
          thumbnailUrl = item.videoThumbnail,
          length = item.videoLength
        )
      }

  override suspend fun getStreamUrl(video: YouTubeVideo): RepoResult<String> {
    return getYouTubeVideoStreamApi.getVideoStream(video.id)
      .let { apiResult -> apiResult.toRepoResult { it.streamUrl } }
  }
}
