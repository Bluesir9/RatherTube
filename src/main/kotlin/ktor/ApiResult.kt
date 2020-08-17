package ktor

import io.ktor.utils.io.errors.IOException
import utils.RepoResult

sealed class ApiResult<out Success, out Failure : GenericErrorResponse> {
  data class Success<Success>(val value: Success) : ApiResult<Success, Nothing>()
  data class Failure<Failure : GenericErrorResponse>(val value: ApiException<Failure>) : ApiResult<Nothing, Failure>()
}

sealed class ApiException<ErrorResponse : GenericErrorResponse> {
  data class NetworkException(val exception: IOException) : ApiException<Nothing>()
  data class ServerException<ErrorResponse : GenericErrorResponse>(val errorResponse: ErrorResponse) :
    ApiException<ErrorResponse>()

  data class UnknownException(val throwable: Throwable) : ApiException<Nothing>()
}

fun <ApiSuccess, ApiFailure : GenericErrorResponse, RepoSuccess> ApiResult<ApiSuccess, ApiFailure>.toRepoResult(
  mapper: (ApiSuccess) -> RepoSuccess
): RepoResult<RepoSuccess> =
  when (this) {
    is ApiResult.Success -> RepoResult.Success(mapper(this.value))
    is ApiResult.Failure -> {
      val errorMessage = when (this.value) {
        /*
        FIXME:
          These strings should be put together in a dedicated place.
          Also, I should try mapping them to a string that be useful
          when showing to the user.
          Also, these error messages should always be logged somewhere
          so that debugging becomes easier.
        */
        is ApiException.NetworkException -> "Network exception encountered"
        is ApiException.ServerException -> this.value.errorResponse.message
        is ApiException.UnknownException -> "Some exception encountered"
      }
      RepoResult.Failure(errorMessage)
    }
  }
