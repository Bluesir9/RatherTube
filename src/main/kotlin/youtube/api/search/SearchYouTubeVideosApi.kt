package youtube.api.search

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import ktor.*

interface SearchYouTubeVideosApi {
  suspend fun search(query: String): ApiResult<SearchYouTubeVideosApiResponse, GenericErrorResponse>
}

class SearchYouTubeVideosApiImpl : SearchYouTubeVideosApi {

  companion object {
    private const val API_URL = "/api/youtube/search"
    private const val PARAM_SEARCH_QUERY = "query"
  }

  override suspend fun search(query: String): ApiResult<SearchYouTubeVideosApiResponse, GenericErrorResponse> {
    val call = suspend {
      httpClient.get<HttpResponse>(
        urlString = API_URL,
        block = {
          parameter(PARAM_SEARCH_QUERY, query)
        })
    }
    return execute(
      call = call,
      successJsonStrategy = SearchYouTubeVideosApiResponse.serializer()
    )
  }
}
