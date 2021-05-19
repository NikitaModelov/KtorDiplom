package ru.modelov.service.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.toxicbakery.bcrypt.Bcrypt
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import ru.modelov.graduatestudents.model.user.UsersTable

data class LoginUser(val email: String, val password: String)
data class TokenPair(val accessToken: String, val refreshToken: String)
data class RefreshToken(val refreshToken: String)
data class RefreshTokenFromDB(val userId: Int, val refreshToken: String, val expiresAt: Long)

fun makeJWTVerifier(algorithm: Algorithm, issuer: String): JWTVerifier = JWT.require(algorithm)
    .withIssuer(issuer)
    .build()

object Tokens : Table("user_tokens") {
    val token = varchar("token", 300)
    override val primaryKey = PrimaryKey(token)
    val userId = (integer("userId") references UsersTable.id)
    val refreshToken = varchar("refreshToken", 300)
    val expiresAt = long("expiresAt")
}

fun ResultRow.toToken() = RefreshTokenFromDB(
    this[Tokens.userId],
    this[Tokens.refreshToken],
    this[Tokens.expiresAt],
)
