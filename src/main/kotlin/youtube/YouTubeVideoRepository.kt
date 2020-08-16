package youtube

interface YouTubeVideoRepository {
  suspend fun getVideos(searchQuery: String): List<YouTubeVideo>
  suspend fun getStreamUrl(video: YouTubeVideo): String
}

/*
TODO:
  Create Ktor client and use it here to hit the
  API server so as to get the actual video content.
*/
object YouTubeVideoRepositoryImpl : YouTubeVideoRepository {
  override suspend fun getVideos(searchQuery: String): List<YouTubeVideo> {
    TODO("Not yet implemented")
  }

  override suspend fun getStreamUrl(video: YouTubeVideo): String {
    TODO("Not yet implemented")
  }
}
