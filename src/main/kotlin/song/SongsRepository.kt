package song

interface SongsRepository {
  suspend fun getSongs(query: String): List<Song>
}
