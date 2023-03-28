package me.kofesst.recipsy.server.common.models.users

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class User(
    val id: Int = -1,
    val nickname: String = "",

    @Transient
    val login: String = "",

    @Transient
    val hashPassword: String = "",

    @Transient
    val accessToken: String = "",

    @Transient
    val tokenSalt: String = "",
)
