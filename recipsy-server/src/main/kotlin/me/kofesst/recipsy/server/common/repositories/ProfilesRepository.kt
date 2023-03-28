package me.kofesst.recipsy.server.common.repositories

import me.kofesst.recipsy.server.common.models.PaginationData
import me.kofesst.recipsy.server.common.models.profiles.UserProfile
import me.kofesst.recipsy.server.common.resources.utils.ApiException

/**
 * Репозиторий, предоставляющий возможность
 * просматривать профили пользователей.
 */
interface ProfilesRepository {
    /**
     * Функция просмотра последний добавленных
     * рецептов. В случае какой-либо ошибки вызывает
     * исключение [ApiException].
     * @throws ApiException
     * @param userId уникальный ID профиля пользователя.
     * @param paginationData настройки пагинации рецептов профиля.
     * @return профиль пользователя [UserProfile], если он существует.
     * Если профиля с таким ID не существует - возвращает null.
     */
    @Throws(ApiException::class)
    suspend fun getProfile(userId: Int, paginationData: PaginationData): UserProfile?
}