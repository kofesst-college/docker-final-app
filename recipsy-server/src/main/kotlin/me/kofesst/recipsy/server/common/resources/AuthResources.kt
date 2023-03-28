package me.kofesst.recipsy.server.common.resources

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import me.kofesst.recipsy.server.common.models.users.UserCredentials
import me.kofesst.recipsy.server.common.repositories.AuthRepository
import me.kofesst.recipsy.server.common.resources.utils.ResponseUtils.respond
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureAuthResources() {
    val repository by inject<AuthRepository>()
    val secretKey = environment.config.property("jwt.secret").getString()
    routing {
        put<SignIn> {
            handleSignIn(repository, secretKey, call.receive())
        }
        post<SignUp> {
            handleSignUp(repository, secretKey, call.receive())
        }
    }
}

@Serializable
@Resource("/sign-up")
class SignUp

suspend fun PipelineContext<Unit, ApplicationCall>.handleSignUp(
    repository: AuthRepository,
    secretKey: String,
    credentials: UserCredentials,
) {
    respond {
        val token = repository.signUp(credentials, secretKey)
        HttpStatusCode.OK to token
    }
}

@Serializable
@Resource("/sign-in")
class SignIn

suspend fun PipelineContext<Unit, ApplicationCall>.handleSignIn(
    repository: AuthRepository,
    secretKey: String,
    credentials: UserCredentials,
) {
    respond {
        val token = repository.signIn(credentials, secretKey)
        HttpStatusCode.OK to token
    }
}