package ru.modelov.graduatestudents.contoller.token

import com.auth0.jwt.JWT
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import ru.modelov.service.auth.TokenPair
import ru.modelov.service.auth.Tokens
import ru.modelov.util.extension.Config
import ru.modelov.withOffset
import java.time.Duration
import java.util.*

class TokenController {

    fun generateTokenPair(userId: Int, isUpdate: Boolean = false): TokenPair {
        val currentTime = System.currentTimeMillis()

        val accessToken = JWT.create()
            .withSubject(userId.toString())
            .withExpiresAt(Date(currentTime.withOffset(Duration.ofMinutes(Config.accessLifetime))))
            .withIssuer(Config.issuer)
            .sign(Config.algorithm)

        val refreshToken = UUID.randomUUID().toString()

        if (!isUpdate) {
            transaction {
                Tokens.insert {
                    it[token] = accessToken
                    it[Tokens.userId] = userId
                    it[Tokens.refreshToken] = refreshToken
                    it[expiresAt] = currentTime.withOffset(Duration.ofDays(Config.refreshLifetime))
                }
            }
            // Todo: Обновление токена в таблице [юзер_токен]
        }
        return TokenPair(accessToken, refreshToken)
    }
}