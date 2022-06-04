package com.emelyanov.icerockpractice.shared.domain.utils

import com.emelyanov.icerockpractice.shared.domain.models.responses.ErrorBody
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException

/**
 * @throws ServerNotRespondingException
 * @throws ConnectionErrorException
 * @throws NotFoundException
 * @throws BadRequestException
 * @throws UnauthorizedException
 */
suspend fun <T> gitRequestWrapper(request: suspend () -> T) : T {
    return try {
        request()
    } catch (ex: SocketTimeoutException) {
        throw ServerNotRespondingException(ex)
    } catch(ex: SocketException) {
        throw ConnectionErrorException(ex)
    } catch (ex: HttpException) {
        when(ex.code()) {
            400 -> throw BadRequestException()
            401 -> {
                val json = Json { ignoreUnknownKeys = true }
                val message = json.decodeFromString<ErrorBody>(ex.response()!!.errorBody()!!.string()).message
                throw UnauthorizedException(message, ex)
            }
            404 -> throw NotFoundException()
            else -> throw ex
        }
    }
}

class ServerNotRespondingException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
class ConnectionErrorException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
class NotFoundException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
class BadRequestException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
class UnauthorizedException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}