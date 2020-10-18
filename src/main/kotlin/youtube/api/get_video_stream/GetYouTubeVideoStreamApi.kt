package youtube.api.get_video_stream

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import ktor.ApiResult
import ktor.GenericErrorResponse
import ktor.execute
import ktor.httpClient

interface GetYouTubeVideoStreamApi {
  suspend fun getVideoStream(videoId: String): ApiResult<GetYouTubeVideoStreamApiResponse, GenericErrorResponse>
}

class GetYouTubeVideoStreamApiImpl : GetYouTubeVideoStreamApi {

  companion object {
    private const val API_URL = "/api/youtube/stream"
    private const val PARAM_VIDEO_ID = "videoId"
  }

  override suspend fun getVideoStream(videoId: String): ApiResult<GetYouTubeVideoStreamApiResponse, GenericErrorResponse> {
    val call = suspend {
      httpClient.get<HttpResponse>(
        urlString = API_URL,
        block = {
          parameter(PARAM_VIDEO_ID, videoId)
        }
      )
    }

    return execute(call, GetYouTubeVideoStreamApiResponse.serializer())
  }
}
