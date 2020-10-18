package logging

interface Logger {
  fun info(message: String)
  fun debug(message: String)
  fun debug(message: String, throwable: Throwable)
  fun error(throwable: Throwable)
}
