package me.kofesst.recipsy.server.common.resources.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<out T : Any>(
    val error: Boolean? = false,
    val message: String? = null,
    val `data`: T? = null,
) : java.io.Serializable

class ApiException(
    val statusCode: HttpStatusCode,
    message: String,
) : Exception(message)

object ResponseUtils {
    suspend inline fun <reified Response : Any> PipelineContext<Unit, ApplicationCall>.respond(
        crossinline block: suspend () -> Pair<HttpStatusCode, Response?>,
    ) {
        val (status, response) = try {
            val response = block()
            response.first to ApiResponse(data = response.second)
        } catch (exception: ApiException) {
            exception.statusCode to ApiResponse(
                error = true,
                message = exception.message
            )
        } catch (exception: Exception) {
            println(exception.stackTraceToString())
            HttpStatusCode.InternalServerError to ApiResponse(
                error = true,
                message = exception.message ?: "Неизвестная ошибка"
            )
        }
        call.respond(status, response)
    }
}