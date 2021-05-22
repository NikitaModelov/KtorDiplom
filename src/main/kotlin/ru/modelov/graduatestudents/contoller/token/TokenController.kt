package ru.modelov.graduatestudents.contoller.token

import com.auth0.jwt.JWT
import com.mongodb.client.model.UpdateOptions
import org.litote.kmongo.combine
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import ru.modelov.graduatestudents.database.DiplomDatabase
import ru.modelov.service.auth.TokenPair
import ru.modelov.service.auth.TokensMongo
import ru.modelov.util.extension.Config
import ru.modelov.withOffset
import java.time.Duration
import java.util.*

class TokenController {

    private val collection = DiplomDatabase.database.getCollection<TokensMongo>("tokens")

    suspend fun generateTokenPair(userId: String, isUpdate: Boolean = false): TokenPair {
        val currentTime = System.currentTimeMillis()

        val accessToken = JWT.create()
            .withSubject(userId)
            .withExpiresAt(Date(currentTime.withOffset(Duration.ofMinutes(Config.accessLifetime))))
            .withIssuer(Config.issuer)
            .sign(Config.algorithm)

        val refreshToken = UUID.randomUUID().toString()

        if (!isUpdate) {
            collection.insertOne(
                TokensMongo(
                    token = accessToken,
                    userId = userId,
                    refreshToken = refreshToken,
                    expiresAt = currentTime.withOffset(Duration.ofDays(Config.refreshLifetime))
                )
            )
        }
        return TokenPair(accessToken, refreshToken)
    }

    suspend fun getRefreshToken(oldRefreshToken: String) =
        collection.findOne(TokensMongo::refreshToken eq oldRefreshToken)

    suspend fun updateToken(tokenPair: TokenPair, oldRefreshToken: String) {
        collection.updateOne(
            TokensMongo::refreshToken eq oldRefreshToken,
            combine(
                setValue(TokensMongo::token, tokenPair.accessToken),
                setValue(TokensMongo::refreshToken, tokenPair.refreshToken),
                setValue(TokensMongo::expiresAt, System.currentTimeMillis().withOffset(Duration.ofDays(Config.refreshLifetime))),
            ),
            UpdateOptions().upsert(false)
        )
    }
}
