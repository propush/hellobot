package com.mindcode.hellobot.localization.exception

open class LocalizationServiceException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}

class LocalizationNotFoundException : LocalizationServiceException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
