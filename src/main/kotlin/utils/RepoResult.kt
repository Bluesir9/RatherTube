package utils

sealed class RepoResult<out T> {
  data class Success<T>(val value: T): RepoResult<T>()
  data class Failure(val message: String): RepoResult<Nothing>()
}
