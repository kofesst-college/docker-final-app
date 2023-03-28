package me.kofesst.recipsy.server.common.models.recipes

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class CookingStage(
    val id: Int = -1,
    val position: Int = 0,
    val description: String = "",
    val imagePath: String? = null,

    @Transient
    val recipeId: Int = 0,
)
