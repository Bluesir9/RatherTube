package config

import io.ktor.http.URLProtocol

val ENVIRONMENT =
  if(js("process.env.NODE_ENV").unsafeCast<String>() == "production") ProductionEnvironment
  else LocalEnvironment

interface Environment {
  val debug: Boolean
  val apiProtocol: URLProtocol
  /*
  As per expected implementation,
  1. It should not contain the http/https
  prefix since that is taken care of by the
  `apiProtocol` Environment variable.
  2. It should not end with a `/` suffix
  since the individual API implementations
  will define it themselves.
  */
  val host: String
}
