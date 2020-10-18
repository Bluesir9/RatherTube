package uuid

data class UUID(val value: String) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is UUID) return false

    if (value != other.value) return false

    return true
  }

  override fun hashCode(): Int {
    return value.hashCode()
  }
}

fun generateUUID(): UUID = UUID(generateUUIDExternal())
