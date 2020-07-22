package logging

interface Logger {
  fun info(message: String)
  fun debug(message: String)
  fun debug(message: String, throwable: Throwable)
  fun error(message: String)
  fun error(message: String, throwable: Throwable)
}
