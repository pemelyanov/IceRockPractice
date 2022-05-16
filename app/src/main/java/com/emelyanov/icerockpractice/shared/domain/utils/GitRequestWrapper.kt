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
        throw ServerNotRespondingException()
    } catch(ex: SocketException) {
        throw ConnectionErrorException()
    } catch (ex: HttpException) {
        when(ex.code()) {
            400 -> throw BadRequestException()
            401 -> {
                val json = Json { ignoreUnknownKeys = true }
                throw UnauthorizedException(json.decodeFromString<ErrorBody>(ex.response()!!.errorBody()!!.string()).message)
            }
            404 -> throw NotFoundException()
            else -> throw ex
        }
    }
}

class ServerNotRespondingException : Exception()
class ConnectionErrorException : Exception()
class NotFoundException : Exception()
class BadRequestException : Exception()
class UnauthorizedException(override val message: String) : Exception(message)