package com.emelyanov.icerockpractice.shared.domain.utils

import com.emelyanov.icerockpractice.shared.domain.models.RequestErrorType
import com.emelyanov.icerockpractice.shared.domain.models.RequestResult
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
suspend fun <T> gitRequestWrapper(request: suspend () -> T) : RequestResult<T> {
    return try {
        RequestResult.Success(data = request())
    } catch (ex: SocketTimeoutException) {
        RequestResult.Error(type = RequestErrorType.ServerNotResponding, exception = ex)
    } catch(ex: SocketException) {
        RequestResult.Error(type = RequestErrorType.ConnectionError, exception = ex)
    } catch (ex: HttpException) {
        processHttpException(ex)
    } catch (ex: Exception) {
        RequestResult.Error(
            message = ex.message,
            type =RequestErrorType.UnknownError,
            exception = ex
        )
    }
}

private fun <T> processHttpException(ex: HttpException) : RequestResult<T>
= when(ex.code()) {
    400 -> RequestResult.Error(type = RequestErrorType.BadRequest, exception = ex)
    401 -> {
        try {
            val json = Json { ignoreUnknownKeys = true }
            val message = json.decodeFromString<ErrorBody>(ex.response()!!.errorBody()!!.string()).message
            RequestResult.Error(
                message = message,
                type = RequestErrorType.Unauthorized,
                exception = ex
            )
        } catch (e: Exception) {
            RequestResult.Error(RequestErrorType.ParseFailed, exception = e)
        }
    }
    404 -> RequestResult.Error(RequestErrorType.NotFound)
    else -> RequestResult.Error(
        message = ex.message,
        type =RequestErrorType.UnknownError,
        exception = ex
    )
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