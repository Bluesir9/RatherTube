package ktor

import config.StringResource
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
        is ApiException.NetworkException -> StringResource.NETWORK_EXCEPTION_ENCOUNTERED
        is ApiException.ServerException -> this.value.errorResponse.message
        is ApiException.UnknownException -> StringResource.SOME_ERROR_ENCOUNTERED
      }
      RepoResult.Failure(errorMessage)
    }
  }
