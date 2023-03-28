package me.kofesst.recipsy.server.common.models.users

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(
    val login: String,
    val password: String,
    val nickname: String? = null,
)
