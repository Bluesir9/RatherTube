package logging

import config.ENVIRONMENT

class LoggerImpl(private val logTag: String) : Logger {

  override fun info(message: String) {
    console.log(logTag, message)
  }

  override fun debug(message: String) {
    if(ENVIRONMENT.debug) {
      console.log(logTag, message)
    }
  }

  override fun debug(message: String, throwable: Throwable) {
    if(ENVIRONMENT.debug) {
      console.log(logTag, message, throwable)
    }
  }

  override fun error(message: String, throwable: Throwable) {
    console.error(logTag, message, throwable)
  }
}
