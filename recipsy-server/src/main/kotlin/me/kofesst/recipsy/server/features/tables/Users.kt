package me.kofesst.recipsy.server.features.tables

import me.kofesst.recipsy.server.common.models.users.User
import org.ktorm.dsl.QueryRowSet
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Users : Table<UserTable>(tableName = "users") {
    val id = int("id_user").primaryKey().bindTo { it.id }
    val login = varchar("login").bindTo { it.login }
    val nickname = varchar("nickname").bindTo { it.nickname }
    val hashPassword = varchar("hash_password").bindTo { it.hashPassword }
    val tokenSalt = varchar("token_salt").bindTo { it.tokenSalt }
    val accessToken = varchar("access_token").bindTo { it.accessToken }
}

interface UserTable : Entity<UserTable> {
    companion object : Entity.Factory<UserTable>()

    val id: Int
    val login: String
    val nickname: String
    val hashPassword: String
    val tokenSalt: String
    val accessToken: String
}

fun QueryRowSet.toUser() = User(
    id = get(Users.id)!!,
    login = get(Users.login)!!,
    nickname = get(Users.nickname)!!,
    hashPassword = get(Users.hashPassword)!!,
    accessToken = get(Users.accessToken)!!,
    tokenSalt = get(Users.tokenSalt)!!,
)