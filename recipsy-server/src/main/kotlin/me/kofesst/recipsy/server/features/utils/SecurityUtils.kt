package me.kofesst.recipsy.server.features.utils

import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object SecurityUtils {
    private const val SALT_LENGTH = 16
    private const val ITERATION_COUNT = 1000
    private const val KEY_LENGTH = 30
    private const val ALGORITHM = "PBKDF2WithHmacSHA1"

    private val RANDOM = SecureRandom()

    fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_LENGTH)
        RANDOM.nextBytes(salt)
        return salt
    }

    fun comparePasswords(password: String, salt: ByteArray, hashPassword: ByteArray): Boolean {
        val pwdHash = generateHashPassword(password, salt)
        if (pwdHash.size != hashPassword.size) return false
        return pwdHash.indices.all { pwdHash[it] == hashPassword[it] }
    }

    fun generateHashPassword(password: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(
            password.toCharArray(),
            salt,
            ITERATION_COUNT,
            KEY_LENGTH
        )
        try {
            val skf = SecretKeyFactory.getInstance(ALGORITHM)
            return skf.generateSecret(spec).encoded
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError("Error while hashing a password: " + e.message, e)
        } catch (e: InvalidKeySpecException) {
            throw AssertionError("Error while hashing a password: " + e.message, e)
        } finally {
            spec.clearPassword()
        }
    }
}