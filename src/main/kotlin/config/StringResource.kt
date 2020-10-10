package config

object StringResource {
  const val RATHERTUBE = "RatherTube"
  const val SEARCH_BOX_PLACEHOLDER = "Search"
  const val NO_SEARCH_RESULTS_FOUND = "No search results found"
  const val NETWORK_EXCEPTION_ENCOUNTERED = "Network exception encountered"
  const val SOME_ERROR_ENCOUNTERED = "Some error encountered"
  const val STREAM_LOAD_FAILED = "Failed to load stream"
  const val PAUSE_FAILED_CAUSE_NO_TRACK_FOUND = "Cannot pause track cause no track was found"
  const val RESUME_FAILED_CAUSE_NO_TRACK_FOUND = "Cannot resume track cause no track was found"
  const val REWIND_FAILED_CAUSE_NO_ACTIVE_TRACK_FOUND = "Cannot rewind track cause no active track was found"
  const val REWIND_FAILED_CAUSE_ACTIVE_TRACK_NOT_FOUND_IN_QUEUE = "Cannot rewind to previous track"

  const val FORWARD_FAILED_CAUSE_NO_ACTIVE_TRACK_FOUND = "Cannot forward track cause no active track was found"
  const val FORWARD_FAILED_CAUSE_NO_TRACKS_AFTER_CURRENT = "No tracks available after current track"
  const val FORWARD_FAILED_CAUSE_ACTIVE_TRACK_NOT_FOUND_IN_QUEUE = "Cannot move forward to next track"

  const val TRACK_ADDED_TO_PLAYBACK_QUEUE = "Track added to playback queue"
  const val FAILED_TO_ADD_TRACK_TO_PLAYBACK_QUEUE = "Failed to add track to playback queue"
  const val FAILED_TO_PLAY_TRACK = "Failed to play track"
  const val FAILED_TO_REMOVE_TRACK_FROM_QUEUE = "Failed to remove track from playback queue"
  const val FAILED_TO_SEEK_CAUSE_NO_ACTIVE_TRACK_FOUND = "Failed to seek cause no active track was found"
}
