package me.kofesst.recipsy.server.common.resources

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable
import me.kofesst.recipsy.server.common.models.PaginationData
import me.kofesst.recipsy.server.common.repositories.ProfilesRepository
import me.kofesst.recipsy.server.common.resources.utils.ResponseUtils.respond
import org.koin.ktor.ext.inject

fun Application.configureProfilesResources() {
    val repository by inject<ProfilesRepository>()
    routing {
        authenticate {
            get<MyProfile> { resource ->
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asInt()
                handleGetProfile(
                    repository = repository,
                    userId = userId,
                    recipesPaginationData = resource.recipesPaginationData
                )
            }
            get<GetProfile> { resource ->
                handleGetProfile(
                    repository = repository,
                    userId = resource.userId,
                    recipesPaginationData = resource.recipesPaginationData
                )
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handleGetProfile(
    repository: ProfilesRepository,
    userId: Int,
    recipesPaginationData: PaginationData,
) {
    respond {
        val profile = repository.getProfile(
            userId,
            recipesPaginationData
        )
        HttpStatusCode.OK to profile
    }
}

@Serializable
@Resource("/users/me")
data class MyProfile(val recipesPaginationData: PaginationData)

@Serializable
@Resource("/users/{userId}")
data class GetProfile(val userId: Int, val recipesPaginationData: PaginationData)