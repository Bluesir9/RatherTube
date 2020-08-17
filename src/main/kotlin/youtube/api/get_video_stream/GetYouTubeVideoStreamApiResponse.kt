package youtube.api.get_video_stream

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val streamUrlField = "stream_url"

@Serializable
data class GetYouTubeVideoStreamApiResponse(
  @SerialName(streamUrlField)
  val streamUrl: String
)
