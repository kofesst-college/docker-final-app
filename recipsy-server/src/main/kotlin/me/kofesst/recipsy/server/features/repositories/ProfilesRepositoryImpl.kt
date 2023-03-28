package me.kofesst.recipsy.server.features.repositories

import me.kofesst.recipsy.server.common.models.PaginationData
import me.kofesst.recipsy.server.common.models.profiles.UserProfile
import me.kofesst.recipsy.server.common.repositories.ProfilesRepository
import me.kofesst.recipsy.server.features.tables.Recipes
import me.kofesst.recipsy.server.features.tables.Users
import me.kofesst.recipsy.server.features.tables.toUser
import me.kofesst.recipsy.server.features.utils.getRecipes
import org.ktorm.database.Database
import org.ktorm.dsl.*

class ProfilesRepositoryImpl(
    private val database: Database,
) : ProfilesRepository {
    override suspend fun getProfile(
        userId: Int,
        paginationData: PaginationData,
    ): UserProfile? {
        val user = database.from(Users)
            .select()
            .where { Users.id eq userId }
            .map { it.toUser() }
            .firstOrNull() ?: return null

        val recipes = database.getRecipes(
            where = { Recipes.authorId eq userId },
            paginationData = paginationData
        )
        return UserProfile(
            user = user,
            recipes = recipes
        )
    }
}