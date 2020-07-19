package uuid

@JsModule("uuid")
@JsNonModule
@JsName("uuidv4")
external fun uuidv4(): String

fun randomUUID(): UUID = UUID(uuidv4())
