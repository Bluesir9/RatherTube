package ktor

import config.ENVIRONMENT
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging

val httpClient = HttpClient(engine = Js.create()) {
  /*
  This features allows us to configure some
  defaults that will be applied to every requests
  sent by this client.
  */
  defaultRequest {
    url {
      protocol = ENVIRONMENT.apiProtocol
      host = ENVIRONMENT.host
    }
  }

  install(JsonFeature) {
    serializer = KotlinxSerializer()
  }

  install(Logging) {
    logger = Logger.DEFAULT
    level = LogLevel.HEADERS
  }

  /*
  FIXME:
    How is this useful?
   */
  install(UserAgent) {
    agent = "RatherTube-Ktor"
  }

  install(HttpTimeout) {
    //10 seconds
    requestTimeoutMillis = 10000
  }
}
