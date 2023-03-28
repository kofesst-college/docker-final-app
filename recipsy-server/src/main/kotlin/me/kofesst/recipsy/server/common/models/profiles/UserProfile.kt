package me.kofesst.recipsy.server.common.models.profiles

import kotlinx.serialization.Serializable
import me.kofesst.recipsy.server.common.models.recipes.Recipe
import me.kofesst.recipsy.server.common.models.users.User

@Serializable
data class UserProfile(
    val user: User = User(),
    val recipes: List<Recipe> = emptyList(),
)
