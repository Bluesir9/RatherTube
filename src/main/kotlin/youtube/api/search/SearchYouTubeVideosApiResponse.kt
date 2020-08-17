package youtube.api.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val itemsField = "items"
private const val videoIdField = "video_id"
private const val videoTitleField = "video_title"
private const val videoArtistField = "video_artist"
private const val videoThumbnailField = "video_thumbnail"

@Serializable
data class SearchYouTubeVideosApiResponse(
  @SerialName(itemsField)
  val items: List<Item>
) {

  @Serializable
  data class Item(
    @SerialName(videoIdField)
    val videoId: String,
    @SerialName(videoTitleField)
    val videoTitle: String,
    @SerialName(videoArtistField)
    val videoArtist: String,
    @SerialName(videoThumbnailField)
    val videoThumbnail: String
  )
}
