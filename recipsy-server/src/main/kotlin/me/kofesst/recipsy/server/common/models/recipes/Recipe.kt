package me.kofesst.recipsy.server.common.models.recipes

import kotlinx.serialization.Serializable
import me.kofesst.recipsy.server.common.models.users.User

@Serializable
data class Recipe(
    val id: Int = 0,
    val name: String = "",
    val imagePath: String? = null,
    val cookingDurationMillis: Int = 0,
    val ingredients: List<Ingredient> = emptyList(),
    val stages: List<CookingStage> = emptyList(),
    val author: User = User(),
)
