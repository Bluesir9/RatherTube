@file:Suppress("EXPERIMENTAL_API_USAGE")

package ktor

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

private val jsonConfiguration =
  JsonConfiguration(
    ignoreUnknownKeys = true,
    /*
    Parser will be strict to malformed input.
    For example boolean values inside quotes
    won't be allowed and string values without
    quotes will also not be allowed.
     */
    isLenient = false,
    /*
    Floating point values like `NaN` and `Infinity`
    won't be allowed
     */
    serializeSpecialFloatingPointValues = false,
    prettyPrint = true
  )

val json: Json = Json(configuration = jsonConfiguration)
