package me.kofesst.recipsy.server.features.repositories

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import me.kofesst.recipsy.server.common.models.AuthResponse
import me.kofesst.recipsy.server.common.models.users.UserCredentials
import me.kofesst.recipsy.server.common.repositories.AuthRepository
import me.kofesst.recipsy.server.common.resources.utils.ApiException
import me.kofesst.recipsy.server.features.tables.Users
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.update
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.mindrot.jbcrypt.BCrypt

class AuthRepositoryImpl(
    private val database: Database,
) : AuthRepository {
    override suspend fun signIn(
        credentials: UserCredentials,
        secretKey: String,
    ): AuthResponse {
        val user = database
            .sequenceOf(Users)
            .find { it.login eq credentials.login }
            ?: throw ApiException(
                statusCode = HttpStatusCode.BadRequest,
                message = "Пользователь не найден"
            )
        if (!BCrypt.checkpw(credentials.password, user.hashPassword)) {
            throw ApiException(
                statusCode = HttpStatusCode.BadRequest,
                message = "Неверный пароль"
            )
        }
        val newToken = JWT.create()
            .withClaim("userId", user.id)
            .sign(Algorithm.HMAC256(secretKey))
        database.update(Users) {
            set(it.accessToken, newToken)
        }
        return AuthResponse(user.id, newToken)
    }

    override suspend fun signUp(
        credentials: UserCredentials,
        secretKey: String,
    ): AuthResponse {
        if (credentials.nickname == null) {
            throw ApiException(
                statusCode = HttpStatusCode.BadRequest,
                message = "Не все поля заполнены"
            )
        }
        val loginCheck = database
            .sequenceOf(Users)
            .find { it.login eq credentials.login }
        if (loginCheck != null) {
            throw ApiException(
                statusCode = HttpStatusCode.BadRequest,
                message = "Данный логин уже занят"
            )
        }
        val passwordSalt = BCrypt.gensalt()
        val hashPassword = BCrypt.hashpw(credentials.password, passwordSalt)
        val userId = database.insertAndGenerateKey(Users) {
            set(it.login, credentials.login)
            set(it.nickname, credentials.nickname)
            set(it.hashPassword, hashPassword)
            set(it.tokenSalt, passwordSalt)
            set(it.accessToken, "invalid")
        } as Int
        val token = JWT.create()
            .withClaim("userId", userId)
            .sign(Algorithm.HMAC256(secretKey))
        database.update(Users) {
            set(it.accessToken, token)
            where {
                it.id eq userId
            }
        }
        return AuthResponse(userId, token)
    }
}