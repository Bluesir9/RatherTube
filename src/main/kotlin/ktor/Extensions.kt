package ktor

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.DeserializationStrategy

suspend fun <Success> execute(
  call: suspend () -> HttpResponse,
  successJsonStrategy: DeserializationStrategy<Success>
): ApiResult<Success, GenericErrorResponse> =
  try {
    call().deserialize(successJsonStrategy)
  } catch (e: Exception) {
    when (e) {
      is IOException -> ApiResult.Failure(ApiException.NetworkException(e))
      else -> ApiResult.Failure(ApiException.UnknownException(e))
    }
  }

private suspend fun <Success> HttpResponse.deserialize(
  successStrategy: DeserializationStrategy<Success>
): ApiResult<Success, GenericErrorResponse> =
  deserialize(
    successStrategy = successStrategy,
    failureStrategy = GenericErrorResponse.serializer()
  )

private suspend fun <Success, Failure: GenericErrorResponse> HttpResponse.deserialize(
  successStrategy: DeserializationStrategy<Success>,
  failureStrategy: DeserializationStrategy<Failure>
): ApiResult<Success, Failure> {
  val response = readText()

  /*
  500 and above errors will be thrown earlier
  and will never enter this phase.
  */
  return if (status.value in 300..499) {
    val failure = json.parse(failureStrategy, response)
    ApiResult.Failure(ApiException.ServerException(failure))
  } else {
    val success = json.parse(successStrategy, response)
    ApiResult.Success(success)
  }
}
