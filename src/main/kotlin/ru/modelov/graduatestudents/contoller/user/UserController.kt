package ru.modelov.graduatestudents.contoller.user

import com.mongodb.client.model.UpdateOptions
import org.litote.kmongo.combine
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import ru.modelov.graduatestudents.contoller.user.rules.Rules
import ru.modelov.graduatestudents.database.DiplomDatabase
import ru.modelov.graduatestudents.model.user.mongo.SecretUser
import ru.modelov.graduatestudents.model.user.mongo.User
import ru.modelov.graduatestudents.model.user.mongo.UserRules
import ru.modelov.graduatestudents.model.user.mongo.toSecretUser
import ru.modelov.service.auth.TokensMongo
import ru.modelov.util.exception.NoRelevantRules

class UserController {

    private val userCollection = DiplomDatabase.database.getCollection<User>("users")
    private val tokenCollection = DiplomDatabase.database.getCollection<TokensMongo>("tokens")

    suspend fun createUser(newUser: User, password: ByteArray) = userCollection.insertOne(
        newUser.copy(
            password = password
        )
    )

    suspend fun getUserByTokenMongo(token: String): SecretUser? {
        val userId = tokenCollection
            .findOne(TokensMongo::token eq token)?.userId

        return userCollection.findOne(User::_id eq userId)?.toSecretUser()
    }

    suspend fun isExistUserByEmail(email: String): Boolean {
        return userCollection.findOne(User::email eq email) != null
    }

    suspend fun getUserByEmail(email: String): User? = userCollection.findOne(User::email eq email)

    suspend fun getRulesByToken(token: String): List<Rules> {
        val userId = tokenCollection
            .findOne(TokensMongo::token eq token)?.userId

        return userCollection.findOne(User::_id eq userId)?.rules?.map {
            Rules.findByValue(it)
        } ?: listOf(Rules.USER)
    }

    suspend fun updateUserRule(newRules: UserRules, token: String) {
        val rules = getRulesByToken(token)

        if (rules.contains(Rules.ADMIN)) {
            userCollection.updateOne(
                User::_id eq newRules._id,
                setValue(User::rules, newRules.rules),
                UpdateOptions().upsert(false)
            )
        } else {
            throw NoRelevantRules()
        }
    }

    suspend fun updateUser(user: SecretUser, token: String) {
        val rules = getRulesByToken(token)
        val isAccess = rules.contains(Rules.ADMIN) || rules.contains(Rules.MODERATOR)

        if (isAccess) {
            updateUserAll(user)
        } else {
            updateUserLimited(user)
        }
    }

    private suspend fun updateUserAll(user: SecretUser) {
        userCollection.updateOne(
            User::_id eq user._id,
            combine(
                setValue(User::firstName, user.firstName),
                setValue(User::secondName, user.secondName),
                setValue(User::patronymic, user.patronymic),
                setValue(User::locate, user.locate),
                setValue(User::birthday, user.birthday),
                setValue(User::achievement, user.achievement),
                setValue(User::workPlace, user.workPlace),
                setValue(User::imageUrl, user.imageUrl),
                setValue(User::yearGraduate, user.yearGraduate),
                setValue(User::scope, user.scope),
                setValue(User::links, user.links),
                setValue(User::active, user.active)
            )
        )
    }

    private suspend fun updateUserLimited(user: SecretUser) {
        userCollection.updateOne(
            User::_id eq user._id,
            combine(
                setValue(User::locate, user.locate),
                setValue(User::workPlace, user.workPlace),
                setValue(User::imageUrl, user.imageUrl),
                setValue(User::links, user.links)
            )
        )
    }
}