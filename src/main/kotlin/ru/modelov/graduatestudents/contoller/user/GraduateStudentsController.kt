package ru.modelov.graduatestudents.contoller.user

import org.litote.kmongo.div
import org.litote.kmongo.eq
import ru.modelov.graduatestudents.database.DiplomDatabase
import ru.modelov.graduatestudents.model.user.mongo.*

class GraduateStudentsController {

    private val userCollection = DiplomDatabase.database.getCollection<User>("users")

    suspend fun getAllGraduate(year: String, faculty: String, group: String): List<UserCard> =
        if (group.isBlank()) {
            userCollection.find(
                User::yearGraduate eq year,
                User::scope / ScopeGroup::faculty eq faculty
            ).toList().map(User::toUserCard)
        } else {
            userCollection.find(
                User::yearGraduate eq year,
                User::scope / ScopeGroup::group eq group,
                User::scope / ScopeGroup::faculty eq faculty
            ).toList().map(User::toUserCard)
        }


    suspend fun getUserById(userId: String): SecretUser? =
        userCollection.findOne(User::_id eq userId)?.toSecretUser()
}
