package ktor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val errorCodeField = "error_code"
private const val errorMessageField = "error_message"

@Serializable
open class GenericErrorResponse(
  @SerialName(errorCodeField)
  val code: Int,

  @SerialName(errorMessageField)
  val message: String
)
