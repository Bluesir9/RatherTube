package playback.queue

import uuid.UUID
import youtube.YouTubeVideo

data class PlaybackQueueItem(val id: UUID, val video: YouTubeVideo)
