package me.kofesst.recipsy.server.common.resources

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import me.kofesst.recipsy.server.common.models.PaginationData
import me.kofesst.recipsy.server.common.models.recipes.Recipe
import me.kofesst.recipsy.server.common.repositories.RecipesRepository
import me.kofesst.recipsy.server.common.resources.utils.ResponseUtils.respond
import org.koin.ktor.ext.inject

fun Application.configureRecipesResources() {
    val repository by inject<RecipesRepository>()
    routing {
        authenticate {
            get<GetRecipes> { resource ->
                respond {
                    val latest = repository.getLatest(resource.paginationData)
                    HttpStatusCode.OK to latest
                }
            }
            get<GetRecipe> { resource ->
                respond {
                    val recipe = repository.get(resource.recipeId)
                    HttpStatusCode.OK to recipe
                }
            }
            post<CreateRecipe> {
                respond {
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal!!.payload.getClaim("userId").asInt()
                    repository.create(call.receive(), userId)
                    HttpStatusCode.OK to Unit
                }
            }
            put<UpdateRecipe> { resource ->
                respond {
                    repository.update(
                        recipe = call.receive<Recipe>().copy(
                            id = resource.recipeId
                        )
                    )
                    HttpStatusCode.OK to Unit
                }
            }
        }
    }
}

@Serializable
@Resource("/recipes/create")
class CreateRecipe

@Serializable
@Resource("/recipes/update/{recipeId}")
class UpdateRecipe(val recipeId: Int)

@Serializable
@Resource("/recipes")
class GetRecipes(val paginationData: PaginationData)

@Serializable
@Resource("/recipes/{recipeId}")
class GetRecipe(val recipeId: Int)