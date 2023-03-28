package me.kofesst.recipsy.server.common.repositories

import me.kofesst.recipsy.server.common.models.AuthResponse
import me.kofesst.recipsy.server.common.models.profiles.UserProfile
import me.kofesst.recipsy.server.common.models.users.UserCredentials
import me.kofesst.recipsy.server.common.resources.utils.ApiException

/**
 * Репозиторий, предоставляющий возможность
 * авторизации и регистрации в системе.
 */
interface AuthRepository {
    /**
     * Функция авторизации по данным для входа, возвращающая
     * новый сгенерированный API токен. В случае какой-либо
     * ошибки вызывает исключение [ApiException].
     * @throws ApiException
     * @param credentials данные для входа пользователя.
     * @param secretKey секретный ключ приложения для хеширования пароля.
     * @return модель [AuthResponse] с ID и токеном пользователя.
     */
    @Throws(ApiException::class)
    suspend fun signIn(credentials: UserCredentials, secretKey: String): AuthResponse

    /**
     * Функция регистрации пользователя, возвращающая API токен.
     * В случае какой-либо ошибки вызывает исключение [ApiException].
     * @throws ApiException
     * @param credentials данные профиля пользователя.
     * @param secretKey секретный ключ приложения для хеширования пароля.
     * @return модель [AuthResponse] с ID и токеном пользователя.
     */
    @Throws(ApiException::class)
    suspend fun signUp(credentials: UserCredentials, secretKey: String): AuthResponse
}