package me.kofesst.recipsy.server.common.repositories

import me.kofesst.recipsy.server.common.models.PaginationData
import me.kofesst.recipsy.server.common.models.recipes.Recipe
import me.kofesst.recipsy.server.common.resources.utils.ApiException

/**
 * Репозиторий, предоставляющий возможность
 * просматривать, добавлять, изменять и удалять рецепты.
 */
interface RecipesRepository {
    /**
     * Функция просмотра последних добавленных
     * рецептов. В случае какой-либо ошибки вызывает
     * исключение [ApiException].
     * @throws ApiException
     * @param paginationData настройки пагинации.
     * @return список последних добавленных рецептов [Recipe].
     */
    @Throws(ApiException::class)
    suspend fun getLatest(paginationData: PaginationData): List<Recipe>

    /**
     * Функция просмотра рецептов определенного
     * автора. В случае какой-либо ошибки вызывает
     * исключение [ApiException].
     * @throws ApiException
     * @param authorId ID автора.
     * @param paginationData настройки пагинации.
     * @return список рецептов [Recipe].
     */
    @Throws(ApiException::class)
    suspend fun getOwned(authorId: Int, paginationData: PaginationData): List<Recipe>

    /**
     * Функция просмотра рецепта.
     * В случае какой-либо ошибки вызывает
     * исключение [ApiException].
     * @throws ApiException
     * @param recipeId ID рецепта.
     * @return рецепт [Recipe].
     */
    @Throws(ApiException::class)
    suspend fun get(recipeId: Int): Recipe

    /**
     * Функция добавления рецепта.
     * В случае какой-либо ошибки вызывает
     * исключение [ApiException].
     * @throws ApiException
     * @param recipe объект рецепта.
     * @param authorId ID автора.
     */
    @Throws(ApiException::class)
    suspend fun create(recipe: Recipe, authorId: Int)

    /**
     * Функция обновления рецепта.
     * В случае какой-либо ошибки вызывает
     * исключение [ApiException].
     * @throws ApiException
     * @param recipe объект рецепта.
     */
    @Throws(ApiException::class)
    suspend fun update(recipe: Recipe)
}