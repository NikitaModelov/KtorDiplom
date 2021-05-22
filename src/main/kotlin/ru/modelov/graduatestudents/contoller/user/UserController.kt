package ru.modelov.graduatestudents.contoller.user

import org.litote.kmongo.eq
import ru.modelov.graduatestudents.database.DiplomDatabase
import ru.modelov.graduatestudents.model.user.mongo.SecretUser
import ru.modelov.graduatestudents.model.user.mongo.User
import ru.modelov.graduatestudents.model.user.mongo.toSecretUser
import ru.modelov.service.auth.TokensMongo

class UserController {

    private val collectionUser = DiplomDatabase.database.getCollection<User>("users")
    private val collectionToken = DiplomDatabase.database.getCollection<TokensMongo>("tokens")

    suspend fun createUser(newUser: User, password: ByteArray) = collectionUser.insertOne(
        newUser.copy(
            password = password
        )
    )

    suspend fun getUserByTokenMongo(token: String): SecretUser? {
        val userId = collectionToken
            .findOne(TokensMongo::token eq token)?.userId

        return collectionUser.findOne(User::_id eq userId)?.toSecretUser()
    }

    suspend fun isExistUserByEmail(email: String): Boolean {
        return collectionUser.findOne(User::email eq email) != null
    }

    suspend fun getUserByEmail(email: String): User? = collectionUser.findOne(User::email eq email)
}