package me.kofesst.recipsy.server.common.models

import kotlinx.serialization.Serializable

@Serializable
data class PaginationData(
    val offset: Int = 0,
    val limit: Int = 10,
)
