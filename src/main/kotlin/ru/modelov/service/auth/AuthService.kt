package ru.modelov.service.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.bson.codecs.pojo.annotations.BsonId

data class LoginUser(val email: String, val password: String)

data class TokenPair(val accessToken: String, val refreshToken: String)

data class RefreshToken(val refreshToken: String)

fun makeJWTVerifier(algorithm: Algorithm, issuer: String): JWTVerifier = JWT.require(algorithm)
    .withIssuer(issuer)
    .build()


data class TokensMongo(
    @BsonId
    val _id: String? = null,
    val token: String,
    val userId: String,
    val refreshToken: String,
    val expiresAt: Long
)
