package me.kofesst.recipsy.server.common.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val userId: Int = -1,
    val apiToken: String = "",
)
