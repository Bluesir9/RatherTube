package uuid

@JsModule("uuid")
@JsNonModule
@JsName("v4")
external val uuidv4: String

fun randomUUID(): UUID = UUID(uuidv4)
